package com.idle.network.model.chatting

import com.idle.domain.model.chatting.ChatMessage
import com.idle.domain.model.chatting.Content
import com.idle.domain.model.chatting.ContentType
import com.idle.domain.model.chatting.SenderType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ChatMessageResponse(
    val id: String? = null,
    val roomId: String? = null,
    val senderId: String? = null,
    val senderType: String? = null,
    val contents: List<ContentResponse>? = null,
    val createdAt: String? = null,
) {
    fun toVO() = ChatMessage(
        id = id ?: "-1",
        roomId = roomId ?: "-1",
        senderId = senderId ?: "-1",
        senderType = SenderType.create(senderType),
        contents = contents?.map { it.toVO() } ?: emptyList(),
        createdAt = createdAt?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) }
            ?: LocalDateTime.MIN,
    )
}

data class ContentResponse(
    val type: String? = null,
    val value: String? = null,
) {
    fun toVO() = Content(
        type = ContentType.create(type),
        value = value ?: "",
    )
}