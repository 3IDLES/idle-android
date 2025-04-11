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
import com.idle.chatting_detail.component.CareChatReceiverTextBubble
import com.idle.chatting_detail.component.CareChatSenderTextBubble
import com.idle.chatting_detail.component.CareChatSenderTextBubbleWithImage
import com.idle.chatting_detail.component.CareChatTextField
import com.idle.compose.addFocusCleaner
import com.idle.compose.clickable
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.util.formatYearMonthDate

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

    val lastVisibleIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size - 1
        }
    }

    val isNearStart by remember { derivedStateOf { listState.firstVisibleItemIndex < 2 } }
    val isNearEnd = lastVisibleIndex >= chatMessages.size.minus(3)

    LaunchedEffect(isNearStart) {
        if (chatMessages.isNotEmpty() && isNearStart) {
            getChatMessages()
        }
    }

    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty() && isNearEnd) {
            listState.animateScrollToItem(chatMessages.size - 1)
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
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(CareTheme.colors.gray050)
                    .padding(horizontal = 18.dp),
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
                    val showDate = index == 0 ||
                            chatMessage.createdAt.formatYearMonthDate() != chatMessages[index - 1].createdAt.formatYearMonthDate()
                    if (showDate) {
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
                            onSeeAllChatClicked = {
                                navigateTo(
                                    com.idle.navigation.DeepLinkDestination.SeeAllChat(
                                        it
                                    )
                                )
                            },
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
                                imageUrl = when (myUserType) {
                                    UserType.CENTER -> workerProfile.profileImageUrl
                                    UserType.WORKER -> centerProfile.profileImageUrl
                                },
                                senderName = when (myUserType) {
                                    UserType.CENTER -> workerProfile.workerName
                                    UserType.WORKER -> centerProfile.centerName
                                },
                                chatMessage = chatMessage,
                                isLast = isLast,
                                onSeeAllChatClicked = {
                                    navigateTo(
                                        com.idle.navigation.DeepLinkDestination.SeeAllChat(
                                            it
                                        )
                                    )
                                },
                                modifier = Modifier.padding(padding),
                            )
                        } else {
                            CareChatSenderTextBubble(
                                chatMessage = chatMessage,
                                isLast = isLast,
                                isRead = isLast,
                                onSeeAllChatClicked = {
                                    navigateTo(
                                        com.idle.navigation.DeepLinkDestination.SeeAllChat(
                                            it
                                        )
                                    )
                                },
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
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(throttleTime = 1000L) { sendMessage() },
                )
            }
        }
    }
}
