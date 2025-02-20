package com.idle.network.model.chat

import com.idle.domain.model.chat.ChatMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ChatMessageResponse(
    val id: String?,
    val chatRoomId: String?,
    val senderId: String?,
    val receiverId: String?,
    val content: String?,
    val createdAt: String?,
    val isRead: Boolean?,
) {
    fun toVO() = ChatMessage(
        id = id ?: "-1",
        roomId = chatRoomId ?: "-1",
        senderId = senderId ?: "-1",
        receiverId = receiverId ?: "",
        content = content ?: "",
        createdAt = createdAt?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) }
            ?: LocalDateTime.MIN,
        isRead = isRead ?: false,
    )
}

