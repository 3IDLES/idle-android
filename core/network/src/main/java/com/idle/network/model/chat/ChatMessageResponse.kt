package com.idle.network.model.chat

import com.idle.domain.model.chat.ChatMessage
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class ChatMessageResponse(
    val id: String?,
    val chatroomId: String?,
    val senderId: String?,
    val receiverId: String?,
    val content: String?,
    val createdAt: String?,
    val sequence: Int?,
    override val type: String = MESSAGE_TYPE,
) : ChatResponse() {
    override fun toVO() = ChatMessage(
        id = id ?: "-1",
        roomId = chatroomId ?: "-1",
        senderId = senderId ?: "-1",
        receiverId = receiverId ?: "",
        content = content ?: "",
        createdAt = createdAt?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) }
            ?: LocalDateTime.MIN,
        isRead = false,
        sequence = sequence ?: -1
    )
}

