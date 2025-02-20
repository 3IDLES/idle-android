package com.idle.network.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class GenerateChatRoomResponse(
    val chatRoomId: String = "",
) {
    fun toVO(): String = chatRoomId
}
