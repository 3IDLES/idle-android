package com.idle.network.model.chat

import com.idle.domain.model.chat.ChatMessage
import kotlinx.serialization.Serializable

@Serializable
data class GetChatMessageResponse(
    val chatMessageInfos: List<ChatMessageResponse> = emptyList(),
    val sequence: Int = 0,
) {
    fun toVO(): Pair<List<ChatMessage>, Int> =
        chatMessageInfos.map(ChatMessageResponse::toVO) to sequence
}
