package com.idle.chatting_detail.component

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.chatting.ChatMessage
import com.idle.domain.model.chatting.Content
import com.idle.domain.model.chatting.ContentType
import com.idle.domain.model.chatting.SenderType
import com.idle.domain.util.formatTimeToHourMinute24
import java.time.LocalDateTime

@Composable
fun CareChatSenderTextBubbleWithImage(
    imageUrl: String?,
    senderName: String,
    chatMessage: ChatMessage,
    isLast: Boolean,
    modifier: Modifier = Modifier,
    isRead: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = imageUrl
                ?: painterResource(com.idle.designresource.R.drawable.ic_notification_placeholder),
            placeholder =
            painterResource(com.idle.designresource.R.drawable.ic_notification_placeholder),
            error = painterResource(com.idle.designresource.R.drawable.ic_notification_placeholder),
            onError = { Log.d("test", imageUrl.toString()) },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 8.dp)
                .size(40.dp)
                .clip(CircleShape),
        )

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1f, false)
        ) {
            Text(
                text = senderName,
                style = CareTheme.typography.caption1,
                color = CareTheme.colors.black,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 12.dp,
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp
                        )
                    )
                    .background(CareTheme.colors.white000)
            ) {
                Text(
                    text = chatMessage.contents.joinToString { it.value },
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.black,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                )
            }
        }

        if (isLast) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .wrapContentWidth()
                    .align(Alignment.Bottom),
            ) {
                if (isRead) {
                    Text(
                        text = "읽음",
                        style = CareTheme.typography.caption1,
                        color = CareTheme.colors.orange500,
                    )
                }

                Text(
                    text = chatMessage.createdAt.formatTimeToHourMinute24(),
                    style = CareTheme.typography.caption1,
                    color = CareTheme.colors.gray300,
                )
            }
        }
    }
}

@Composable
fun CareChatSenderTextBubble(
    chatMessage: ChatMessage,
    isLast: Boolean,
    modifier: Modifier = Modifier,
    isRead: Boolean = false,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(40.dp),
        )

        Box(
            modifier = Modifier
                .weight(1f, false)
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 12.dp,
                        bottomStart = 12.dp,
                        bottomEnd = 12.dp
                    )
                )
                .background(CareTheme.colors.white000)
        ) {
            Text(
                text = chatMessage.contents.joinToString { it.value },
                style = CareTheme.typography.body3,
                color = CareTheme.colors.black,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            )
        }

        if (isLast) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(start = 4.dp),
            ) {
                if (isRead) {
                    Text(
                        text = "읽음",
                        style = CareTheme.typography.caption1,
                        color = CareTheme.colors.orange500,
                    )
                }

                Text(
                    text = chatMessage.createdAt.formatTimeToHourMinute24(),
                    style = CareTheme.typography.caption1,
                    color = CareTheme.colors.gray300,
                )
            }
        }
    }
}

@Composable
fun CareChatReceiverTextBubble(
    chatMessage: ChatMessage,
    isLast: Boolean,
    modifier: Modifier = Modifier,
    isRead: Boolean = false,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier.fillMaxWidth()
    ) {
        if (isLast) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(end = 4.dp),
            ) {
                if (isRead) {
                    Text(
                        text = "읽음",
                        style = CareTheme.typography.caption1,
                        color = CareTheme.colors.orange500,
                    )
                }

                Text(
                    text = chatMessage.createdAt.formatTimeToHourMinute24(),
                    style = CareTheme.typography.caption1,
                    color = CareTheme.colors.gray300,
                )
            }
        }

        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = 12.dp,
                        bottomEnd = 0.dp
                    )
                )
                .background(CareTheme.colors.orange500),
        ) {
            Text(
                text = chatMessage.contents.joinToString { it.value },
                style = CareTheme.typography.body3,
                color = CareTheme.colors.white000,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = Color.LTGRAY.toLong())
@Composable
fun PreviewCareChatTextBubbleWithImage() {
    val chatMessage = ChatMessage(
        id = "1",
        roomId = "room1",
        senderId = "user1",
        senderType = SenderType.USER,
        contents = listOf(
            Content(type = ContentType.TEXT, value = "안녕하세요! 문의드리고 싶어서 연락드렸습니다.")
        ),
        createdAt = LocalDateTime.now().minusMinutes(5)
    )

    CareChatSenderTextBubbleWithImage(
        imageUrl = "https://via.placeholder.com/40",
        senderName = "요양센터",
        chatMessage = chatMessage,
        isLast = true,
        isRead = true,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    )
}

@Preview(showBackground = true, backgroundColor = Color.LTGRAY.toLong())
@Composable
fun PreviewCareChatTextBubble() {
    val chatMessage = ChatMessage(
        id = "1",
        roomId = "room1",
        senderId = "user1",
        senderType = SenderType.USER,
        contents = listOf(
            Content(type = ContentType.TEXT, value = "안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!")
        ),
        createdAt = LocalDateTime.now().minusMinutes(5)
    )

    CareChatSenderTextBubble(
        chatMessage = chatMessage,
        isLast = true,
        isRead = true,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    )
}

@Preview(showBackground = true, backgroundColor = Color.LTGRAY.toLong())
@Composable
fun PreviewCareCardChatSender() {
    val chatMessage = ChatMessage(
        id = "1",
        roomId = "room1",
        senderId = "user1",
        senderType = SenderType.USER,
        contents = listOf(
            Content(type = ContentType.TEXT, value = "안녕하세요! 문의드리고 싶어서 연락드렸습니다.")
        ),
        createdAt = LocalDateTime.now().minusMinutes(5)
    )

    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        CareChatSenderTextBubbleWithImage(
            imageUrl = "https://via.placeholder.com/40", // Placeholder 이미지 URL
            senderName = "요양센터",
            chatMessage = chatMessage,
            isLast = false,
            isRead = false,
        )

        CareChatSenderTextBubble(
            chatMessage = chatMessage,
            isLast = true,
            isRead = true,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = Color.LTGRAY.toLong())
@Composable
fun PreviewCareCardChatReceiver() {
    val chatMessage = ChatMessage(
        id = "1",
        roomId = "room1",
        senderId = "user1",
        senderType = SenderType.USER,
        contents = listOf(
            Content(type = ContentType.TEXT, value = "안녕하세요! 문의드리고 싶어서 연락드렸습니다.")
        ),
        createdAt = LocalDateTime.now().minusMinutes(5)
    )

    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        CareChatReceiverTextBubble(
            chatMessage = chatMessage,
            isLast = false,
            isRead = false,
        )

        CareChatReceiverTextBubble(
            chatMessage = chatMessage,
            isLast = true,
            isRead = true,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = Color.LTGRAY.toLong())
@Composable
fun PreviewCareCardChatExample() {
    val chatMessage1 = ChatMessage(
        id = "1",
        roomId = "room1",
        senderId = "user1",
        senderType = SenderType.USER,
        contents = listOf(
            Content(type = ContentType.TEXT, value = "안녕하세요! 문의드리고 싶어서 연락드렸습니다.")
        ),
        createdAt = LocalDateTime.now().minusMinutes(5)
    )
    val chatMessage2 = ChatMessage(
        id = "1",
        roomId = "room1",
        senderId = "user1",
        senderType = SenderType.USER,
        contents = listOf(
            Content(type = ContentType.TEXT, value = "안녕하세요! 문의드리고 싶어서 연락드렸습니다.")
        ),
        createdAt = LocalDateTime.now().minusMinutes(5)
    )


    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        CareChatSenderTextBubbleWithImage(
            imageUrl = "https://via.placeholder.com/40", // Placeholder 이미지 URL
            senderName = "요양센터",
            chatMessage = chatMessage1,
            isLast = false,
            isRead = false,
        )

        CareChatSenderTextBubble(
            chatMessage = chatMessage1,
            isLast = true,
            isRead = true,
            modifier = Modifier.padding(top = 6.dp),
        )

        CareChatReceiverTextBubble(
            chatMessage = chatMessage2,
            isLast = false,
            isRead = false,
            modifier = Modifier.padding(top = 16.dp),
        )

        CareChatReceiverTextBubble(
            chatMessage = chatMessage2,
            isLast = true,
            isRead = true,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}