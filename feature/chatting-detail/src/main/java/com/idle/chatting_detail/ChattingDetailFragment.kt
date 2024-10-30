package com.idle.chatting_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.idle.chatting_detail.component.CareChatReceiverTextBubble
import com.idle.chatting_detail.component.CareChatSenderTextBubble
import com.idle.chatting_detail.component.CareChatSenderTextBubbleWithImage
import com.idle.chatting_detail.component.CareChatTextField
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chatting.ChatMessage
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.util.formatYearMonthDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ChattingDetailFragment : BaseComposeFragment() {
    private val args: ChattingDetailFragmentArgs by navArgs()
    override val fragmentViewModel: ChattingDetailViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        val chattingRoomId = args.chattingRoomId
        val receiverUserType = UserType.create(args.receiverUserType)
        val receiverId = args.receiverId
        val senderId = args.senderId

        fragmentViewModel.apply {
            val writingText by writingText.collectAsStateWithLifecycle()
            val chatMessages by chatMessages.collectAsStateWithLifecycle()
            val workerProfile by workerProfile.collectAsStateWithLifecycle()
            val centerProfile by centerProfile.collectAsStateWithLifecycle()

            LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
                getUserProfile(receiverUserType = receiverUserType, senderId = senderId)
                getChatMessages(chattingRoomId)
            }

            if (chatMessages != null && workerProfile != null && centerProfile != null) {
                ChattingDetailScreen(
                    receiverId = receiverId,
                    receiverUserType = receiverUserType,
                    workerProfile = workerProfile!!,
                    centerProfile = centerProfile!!,
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
    receiverUserType: UserType,
    workerProfile: WorkerProfile,
    centerProfile: CenterProfile,
    writingText: String,
    chatMessages: List<ChatMessage>,
    onWritingTextChange: (String) -> Unit,
    navigateUp: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var lastDate: String? = null

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
                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                }

                itemsIndexed(
                    items = chatMessages,
                    key = { _, item -> item.id },
                ) { index, chatMessage ->
                    val isLast = if (index < chatMessages.size - 1) {
                        val nextIndex = index + 1
                        chatMessage.senderId != chatMessages[nextIndex].senderId
                    } else {
                        true
                    }

                    val padding = PaddingValues(bottom = if (isLast) 16.dp else 6.dp)


                    val messageDate = chatMessage.createdAt.formatYearMonthDate()
                    val showDate = lastDate != messageDate
                    if (showDate) {
                        lastDate = messageDate

                        Text(
                            text = messageDate,
                            style = CareTheme.typography.caption1,
                            color = CareTheme.colors.gray700,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = if (index == 0 || isLast) 0.dp else 10.dp,
                                    bottom = 16.dp
                                )
                        )
                    }

                    val isMyMessage = receiverId == chatMessage.senderId
                    if (isMyMessage) {
                        CareChatReceiverTextBubble(
                            chatMessage = chatMessage,
                            isLast = isLast,
                            modifier = Modifier.padding(padding),
                        )
                    } else {
                        val showProfile = if (index > 0) {
                            val previousIndex = index - 1
                            chatMessage.senderId != chatMessages[previousIndex].senderId
                        } else {
                            true
                        }

                        if (showProfile) {
                            CareChatSenderTextBubbleWithImage(
                                imageUrl = when (receiverUserType) {
                                    UserType.CENTER -> workerProfile.profileImageUrl
                                    UserType.WORKER -> centerProfile.profileImageUrl
                                },
                                senderName = when (receiverUserType) {
                                    UserType.CENTER -> workerProfile.workerName
                                    UserType.WORKER -> centerProfile.centerName
                                },
                                chatMessage = chatMessage, isLast = isLast,
                                modifier = Modifier.padding(padding),
                            )
                        } else {
                            CareChatSenderTextBubble(
                                chatMessage = chatMessage,
                                isLast = isLast,
                                isRead = isLast,
                                modifier = Modifier.padding(padding),
                            )
                        }
                    }
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
