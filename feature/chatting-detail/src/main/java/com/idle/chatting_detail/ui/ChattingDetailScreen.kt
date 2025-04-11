package com.idle.chatting_detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.idle.chatting_detail.ui.component.ChattingBody
import com.idle.chatting_detail.ui.component.ChattingInput
import com.idle.compose.addFocusCleaner
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.WorkerProfile

@Composable
internal fun ChattingDetailScreen(
    receiverId: String,
    myUserType: UserType,
    workerProfile: WorkerProfile,
    centerProfile: CenterProfile,
    writingText: String,
    chatMessages: List<ChatMessage>,
    onWritingTextChange: (String) -> Unit,
    sendMessage: () -> Unit,
    getChatMessages: () -> Unit,
    navigateTo: (com.idle.navigation.DeepLinkDestination) -> Unit,
    navigateUp: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()

    // 이전 메시지 페이징 조건
    val isNearStart by remember { derivedStateOf { listState.firstVisibleItemIndex < 2 } }
    LaunchedEffect(isNearStart) {
        if (chatMessages.isNotEmpty() && isNearStart) {
            getChatMessages()
        }
    }

    // 하단에 있을 경우에만 새 메시지가 오면 최하단으로 스크롤
    val lastVisibleIndex by remember {
        derivedStateOf { listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size - 1 }
    }
    val isNearEnd = lastVisibleIndex >= chatMessages.size - 3
    var initialLoad by remember { mutableStateOf(true) }
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty() && (initialLoad || isNearEnd)) {
            listState.animateScrollToItem(chatMessages.size - 1)
            initialLoad = false
        }
    }

    Scaffold(
        topBar = {
            val title = when (myUserType) {
                UserType.WORKER -> centerProfile.centerName
                UserType.CENTER -> workerProfile.workerName
            }
            CareSubtitleTopBar(
                title = title,
                onNavigationClick = navigateUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, end = 20.dp, bottom = 12.dp)
            )
        },
        containerColor = CareTheme.colors.white000,
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            ChattingBody(
                chatMessages = chatMessages,
                receiverId = receiverId,
                myUserType = myUserType,
                workerProfile = workerProfile,
                centerProfile = centerProfile,
                listState = listState,
                navigateTo = navigateTo,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )

            ChattingInput(
                writingText = writingText,
                onWritingTextChange = onWritingTextChange,
                sendMessage = sendMessage,
            )
        }
    }
}
