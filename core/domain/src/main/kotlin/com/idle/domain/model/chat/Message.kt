package com.idle.domain.model.chat

import java.time.LocalDateTime

sealed class Message(open val sequence: Int)

data class ChatMessage(
    val id: String,
    val roomId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val createdAt: LocalDateTime,
    val isRead: Boolean,
    override val sequence: Int,
) : Message(sequence)

data class ReadMessage(
    val opponentId: String,
    val chatroomId: String,
    override val sequence: Int,
) : Message(sequence)
