package com.idle.chatting_detail.ui.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.util.formatTimeToHourMinute24

@Composable
fun CareChatSenderTextBubbleWithImage(
    imageUrl: String?,
    senderName: String,
    chatMessage: ChatMessage,
    isLast: Boolean,
    modifier: Modifier = Modifier,
    isRead: Boolean = false,
    onSeeAllChatClicked: (String) -> Unit = {},
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
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                    var hasOverflow by remember { mutableStateOf(false) }

                    Text(
                        text = chatMessage.content,
                        style = CareTheme.typography.body3,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 10,
                        color = CareTheme.colors.black,
                        onTextLayout = { textLayoutResult ->
                            hasOverflow = textLayoutResult.hasVisualOverflow
                        },
                    )

                    if (hasOverflow) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .clickable { onSeeAllChatClicked(chatMessage.content) },
                        ) {
                            Text(
                                text = "전체보기",
                                style = CareTheme.typography.caption1,
                                color = CareTheme.colors.gray500,
                            )

                            Image(
                                painter = painterResource(com.idle.designresource.R.drawable.ic_arrow_left_small),
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }

        if (isLast) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(start = 6.dp)
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
    onSeeAllChatClicked: (String) -> Unit = {},
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
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                var hasOverflow by remember { mutableStateOf(false) }

                Text(
                    text = chatMessage.content,
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.black,
                    maxLines = 10,
                    onTextLayout = { textLayoutResult ->
                        hasOverflow = textLayoutResult.hasVisualOverflow
                    },
                )

                if (hasOverflow) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .clickable { onSeeAllChatClicked(chatMessage.content) },
                    ) {
                        Text(
                            text = "전체보기",
                            style = CareTheme.typography.caption1,
                            color = CareTheme.colors.gray500,
                        )

                        Image(
                            painter = painterResource(com.idle.designresource.R.drawable.ic_arrow_left_small),
                            contentDescription = null,
                        )
                    }
                }
            }
        }

        if (isLast) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(start = 6.dp),
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
    onSeeAllChatClicked: (String) -> Unit = {},
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
                    .padding(end = 6.dp),
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
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                var hasOverflow by remember { mutableStateOf(false) }

                Text(
                    text = chatMessage.content,
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.white000,
                    maxLines = 10,
                    onTextLayout = { textLayoutResult ->
                        hasOverflow = textLayoutResult.hasVisualOverflow
                    },
                )

                if (hasOverflow) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .clickable { onSeeAllChatClicked(chatMessage.content) },
                    ) {
                        Text(
                            text = "전체보기",
                            style = CareTheme.typography.caption1,
                            color = CareTheme.colors.white000,
                        )

                        Image(
                            painter = painterResource(com.idle.designresource.R.drawable.ic_arrow_left_small),
                            colorFilter = ColorFilter.tint(CareTheme.colors.white000),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}
