package com.idle.domain.repositorry

import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.chat.ChatRoom
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun connectWebSocket(): Result<Unit>
    suspend fun disconnectWebSocket(): Result<Unit>
    suspend fun getChatRooms(userType: UserType): Result<List<ChatRoom>>
    suspend fun getChatRoomMessages(
        userType: UserType,
        roomId: String,
        messageId: String?,
    ): Result<List<ChatMessage>>

    suspend fun generateChatRooms(userType: UserType, opponentId: String): Result<String>
    suspend fun subscribeChatMessage(userId: String): Flow<ChatMessage>
    suspend fun sendMessage(
        chatroomId: String,
        receiverId: String,
        senderName: String,
        content: String,
    ): Result<Unit>

    suspend fun readMessage(
        chatroomId: String,
        opponentId: String,
    ): Result<Unit>
}
