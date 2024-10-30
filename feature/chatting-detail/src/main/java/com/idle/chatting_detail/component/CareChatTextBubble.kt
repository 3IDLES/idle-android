package com.idle.chatting_detail.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.idle.domain.model.chatting.ChatMessage

@Composable
fun CareChatTextBubble(
    chatMessage: ChatMessage,
    modifier: Modifier = Modifier,
    isMyChat: Boolean = false,
) {
    if (isMyChat) {

    } else {

    }
}