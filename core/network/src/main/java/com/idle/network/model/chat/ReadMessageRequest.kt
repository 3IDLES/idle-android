package com.idle.network.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class ReadMessageRequest(
    val chatroomId: String,
    val opponentId: String,
    val messageSequence: Int,
)
