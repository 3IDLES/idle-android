package com.idle.domain.model.chatting

import java.time.LocalDateTime

data class ChatRoom(
    val id: String,
    val sender: String,
    val receiver: String,
    val createdAt: LocalDateTime,
)