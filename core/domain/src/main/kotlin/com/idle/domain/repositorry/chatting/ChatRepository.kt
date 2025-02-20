package com.idle.domain.repositorry.chatting

import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.chat.ChatRoom
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun connectWebSocket(): Result<Unit>
    suspend fun disconnectWebSocket(): Result<Unit>
    suspend fun getChatRooms(userType: UserType): Result<List<ChatRoom>>
    fun subscribeChatMessage(): Flow<ChatMessage>
}
