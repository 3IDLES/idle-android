package com.idle.network.model.chat

import com.idle.domain.model.chat.Message
import kotlinx.serialization.Serializable

@Serializable
sealed class ChatResponse {
    abstract val type: String

    abstract fun toVO(): Message

    companion object {
        const val MESSAGE_TYPE = "MESSAGE"
        const val READ_TYPE = "READ"
    }
}
