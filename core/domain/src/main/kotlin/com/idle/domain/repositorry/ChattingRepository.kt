package com.idle.domain.repositorry

import com.idle.domain.model.chatting.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChattingRepository {
    suspend fun connectWebSocket(): Result<Unit>
    suspend fun disconnectWebSocket(): Result<Unit>
    fun subscribeChatMessage(): Flow<ChatMessage>
}
