package com.idle.domain.repositorry

import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.chat.ChatRoom
import com.idle.domain.model.chat.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun connectWebSocket(): Result<Unit>
    suspend fun disconnectWebSocket(): Result<Unit>

    suspend fun retrieveChatRooms(userId: String): Result<List<ChatRoom>>
    suspend fun loadChatRooms(userId: String, userType: UserType): Result<Unit>

    suspend fun retrieveChatRoomMessages(
        roomId: String,
        messageId: String?,
    ): Result<List<ChatMessage>>

    suspend fun getChatRoomMessages(
        userType: UserType,
        roomId: String,
        messageId: String?,
    ): Result<List<ChatMessage>>

    suspend fun generateChatRooms(userType: UserType, opponentId: String): Result<String>
    suspend fun subscribeChatMessage(userId: String): Flow<Message>
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
