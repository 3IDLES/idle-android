package com.idle.domain.model.chat

import java.time.LocalDateTime

data class ChatRoom(
    val id: String,
    val opponentId: String,
    val lastMessage: String,
    val lastMessageTime: LocalDateTime,
    val unReadMessageCount: Int,
)
