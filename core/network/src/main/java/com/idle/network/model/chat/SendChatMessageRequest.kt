package com.idle.network.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class SendChatMessageRequest(
    val chatroomId: String,
    val receiverId: String,
    val senderName: String,
    val content: String,
)
