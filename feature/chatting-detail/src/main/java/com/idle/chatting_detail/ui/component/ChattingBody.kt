package com.idle.chatting_detail.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.util.formatYearMonthDate
import com.idle.navigation.DeepLinkDestination

@Composable
fun ChattingBody(
    chatMessages: List<ChatMessage>,
    receiverId: String,
    myUserType: UserType,
    workerProfile: WorkerProfile,
    centerProfile: CenterProfile,
    listState: LazyListState,
    navigateTo: (DeepLinkDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        modifier = modifier
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
                chatMessage.senderId != chatMessages[index + 1].senderId
            } else {
                true
            }
            val itemPadding = PaddingValues(bottom = if (isLast) 16.dp else 6.dp)

            val showDate = index == 0 ||
                    chatMessage.createdAt.formatYearMonthDate() !=
                    chatMessages[index - 1].createdAt.formatYearMonthDate()
            if (showDate) {
                DateHeader(
                    dateText = chatMessage.createdAt.formatYearMonthDate(),
                    isFirstOrLast = index == 0 || isLast
                )
            }

            // 메시지 Bubble 결정
            val isMyMessage = receiverId == chatMessage.senderId
            if (isMyMessage) {
                CareChatReceiverTextBubble(
                    chatMessage = chatMessage,
                    isLast = isLast,
                    onSeeAllChatClicked = {
                        navigateTo(
                            DeepLinkDestination.SeeAllChat(it)
                        )
                    },
                    modifier = Modifier.padding(itemPadding),
                )
            } else {
                val showProfile = if (index > 0) {
                    chatMessage.senderId != chatMessages[index - 1].senderId
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
                        onSeeAllChatClicked = { navigateTo(DeepLinkDestination.SeeAllChat(it)) },
                        modifier = Modifier.padding(itemPadding),
                    )
                } else {
                    CareChatSenderTextBubble(
                        chatMessage = chatMessage,
                        isLast = isLast,
                        onSeeAllChatClicked = { navigateTo(DeepLinkDestination.SeeAllChat(it)) },
                        modifier = Modifier.padding(itemPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun DateHeader(
    dateText: String,
    isFirstOrLast: Boolean,
    modifier: Modifier = Modifier
) {
    Text(
        text = dateText,
        style = CareTheme.typography.caption1,
        color = CareTheme.colors.gray700,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = if (isFirstOrLast) 0.dp else 10.dp,
                bottom = 16.dp
            )
    )
}
