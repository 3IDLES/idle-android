package com.idle.chatting_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.idle.chatting_detail.ui.ChattingDetailLoadingScreen
import com.idle.chatting_detail.ui.ChattingDetailScreen
import com.idle.compose.base.BaseComposeFragment
import com.idle.navigation.NavigationEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class ChattingDetailFragment : BaseComposeFragment() {
    override val fragmentViewModel: ChattingDetailViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        val scope = rememberCoroutineScope()

        fragmentViewModel.apply {
            val receiverUserType = myUserType
            val receiverId = myId

            val writingText by writingText.collectAsStateWithLifecycle()
            val chatMessages by chatMessages.collectAsStateWithLifecycle()
            val workerProfile by workerProfile.collectAsStateWithLifecycle()
            val centerProfile by centerProfile.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                getUserProfile()
                getChatMessages()
                readMessage()
            }

            LifecycleStartEffect(fragmentViewModel) {
                connectWebsocket()
                onStopOrDispose { disconnectWebsocket() }
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
                    getChatMessages = { scope.launch { getChatMessages() } },
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
