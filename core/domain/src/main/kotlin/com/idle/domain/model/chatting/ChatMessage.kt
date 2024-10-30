package com.idle.domain.model.chatting

import java.time.LocalDateTime

data class ChatMessage(
    val id: String,
    val roomId: String,
    val senderId: String,
    val senderType: SenderType,
    val contents: List<Content>,
    val createdAt: LocalDateTime,
)

data class Content(
    val type: ContentType,
    val value: String,
)

enum class SenderType {
    USER,
}

enum class ContentType {
    TEXT, IMAGE;
}
