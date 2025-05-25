package com.idle.network.model.chat

import com.idle.domain.model.chat.ReadMessage
import kotlinx.serialization.Serializable

@Serializable
data class ReadMessageResponse(
    val readByUserId: String?,
    val chatroomId: String?,
    val messageSequence: Int?,
    override val type: String = READ_TYPE,
) : ChatResponse() {
    override fun toVO() = ReadMessage(
        opponentId = readByUserId ?: "",
        chatroomId = chatroomId ?: "",
        sequence = messageSequence ?: -1
    )
}
