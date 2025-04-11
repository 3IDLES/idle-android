package com.idle.domain.model.chat

import java.time.LocalDateTime

interface Message

data class ChatMessage(
    val id: String,
    val roomId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val createdAt: LocalDateTime,
    val isRead: Boolean,
) : Message

data class ReadMessage(
    val opponentId: String,
    val chatroomId: String,
) : Message
