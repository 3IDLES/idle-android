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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.idle.chatting_detail.component.CareChatReceiverTextBubble
import com.idle.chatting_detail.component.CareChatSenderTextBubble
import com.idle.chatting_detail.component.CareChatSenderTextBubbleWithImage
import com.idle.chatting_detail.component.CareChatTextField
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.util.formatYearMonthDate
import com.idle.navigation.NavigationEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ChattingDetailFragment : BaseComposeFragment() {
    override val fragmentViewModel: ChattingDetailViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val receiverUserType = receiverUserType
            val receiverId = receiverId

            val writingText by writingText.collectAsStateWithLifecycle()
            val chatMessages by chatMessages.collectAsStateWithLifecycle()
            val workerProfile by workerProfile.collectAsStateWithLifecycle()
            val centerProfile by centerProfile.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                getUserProfile()
                getChatMessages()
                subscribeChatMessage()
                readMessage()
            }

            if (chatMessages != null && workerProfile != null && centerProfile != null) {
                ChattingDetailScreen(
                    receiverId = receiverId,
                    myUserType = receiverUserType,
                    workerProfile = workerProfile!!,
                    centerProfile = centerProfile!!,
                    writingText = writingText,
                    chatMessages = chatMessages!!,
                    onWritingTextChange = ::setWritingText,
                    getChatMessages = ::getChatMessages,
                    sendMessage = ::sendMessage,
                    navigateTo = { navigationHelper.navigateTo(NavigationEvent.To(it)) },
                    navigateUp = { findNavController().navigateUp() }
                )
            } else {
                ChattingDetailLoadingScreen(navigateUp = { findNavController().navigateUp() })
            }
        }
    }
}
