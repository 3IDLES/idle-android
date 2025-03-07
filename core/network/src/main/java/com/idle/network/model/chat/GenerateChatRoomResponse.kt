package com.idle.network.model.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerateChatRoomResponse(
    @SerialName("opponentId") val chatRoomId: String = "",
) {
    fun toVO(): String = chatRoomId
}
