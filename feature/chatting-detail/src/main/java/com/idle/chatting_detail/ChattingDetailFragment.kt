package com.idle.chatting_detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.idle.chatting_detail.component.CareChatTextBubble
import com.idle.chatting_detail.component.CareChatTextField
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chatting.ChatMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ChattingDetailFragment : BaseComposeFragment() {
    private val args: ChattingDetailFragmentArgs by navArgs()
    override val fragmentViewModel: ChattingDetailViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        val chattingRoomId = rememberSaveable { args.chattingRoomId }
        val receiverUserType = rememberSaveable { UserType.create(args.receiverUserType) }
        val receiverId = rememberSaveable { args.receiverId }
        val senderId = rememberSaveable { args.senderId }

        LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
            Log.d("test", "$chattingRoomId $receiverId $receiverUserType $senderId")
            // Todo
        }

        fragmentViewModel.apply {
            val writingText by writingText.collectAsStateWithLifecycle()
            val chatMessages by chatMessages.collectAsStateWithLifecycle()

            if (chatMessages != null) {
                ChattingDetailScreen(
                    receiverId = receiverId,
                    writingText = writingText,
                    chatMessages = chatMessages!!,
                    onWritingTextChange = ::setWritingText,
                    navigateUp = { findNavController().navigateUp() }
                )
            } else {
                ChattingDetailLoadingScreen(
                    navigateUp = { findNavController().navigateUp() },
                )
            }
        }
    }
}

@Composable
internal fun ChattingDetailScreen(
    receiverId: String,
    writingText: String,
    chatMessages: List<ChatMessage>,
    onWritingTextChange: (String) -> Unit,
    navigateUp: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CareSubtitleTopBar(
                title = "세얼간이요양센터",
                onNavigationClick = navigateUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, end = 20.dp, bottom = 12.dp),
            )
        },
        containerColor = CareTheme.colors.white000,
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(CareTheme.colors.gray050)
                    .padding(horizontal = 20.dp),
            ) {
                items(
                    items = chatMessages,
                    key = { it.id },
                ) { chatMessage ->
                    CareChatTextBubble(
                        chatMessage = chatMessage,
                        isMyChat = chatMessage.senderId == receiverId,
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 16.dp)
            ) {
                CareChatTextField(
                    value = writingText,
                    onValueChanged = onWritingTextChange,
                    hint = "메세지를 입력하세요.",
                    modifier = Modifier.weight(1f),
                )

                Image(
                    painter = painterResource(com.idle.designresource.R.drawable.ic_send_message),
                    contentDescription = "",
                    modifier = Modifier.size(32.dp),
                )
            }
        }
    }
}
