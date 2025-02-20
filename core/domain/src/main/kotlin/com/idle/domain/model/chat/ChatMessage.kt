package com.idle.domain.model.chat

import java.time.LocalDateTime

data class ChatMessage(
    val id: String,
    val roomId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val createdAt: LocalDateTime,
    val isRead: Boolean,
)

enum class SenderType {
    USER, UNKNOWN;

    companion object {
        fun create(value: String?): SenderType {
            return SenderType.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}

enum class ContentType {
    TEXT, IMAGE, UNKNOWN;

    companion object {
        fun create(value: String?): ContentType {
            return ContentType.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}
