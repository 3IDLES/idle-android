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
