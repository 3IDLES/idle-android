package com.idle.domain.repositorry

import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.chat.ChatRoom
import com.idle.domain.model.chat.ChatRoomWithOpponentInfo
import com.idle.domain.model.chat.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun connectWebSocket()
    suspend fun disconnectWebSocket()
    suspend fun retrieveChatRooms(userId: String): List<ChatRoom>
    suspend fun loadChatRooms(
        userId: String,
        userType: UserType
    ): List<ChatRoomWithOpponentInfo>

    suspend fun retrieveChatRoomMessages(
        roomId: String,
        myId: String,
        messageId: String?,
    ): List<ChatMessage>

    suspend fun getChatRoomMessages(
        userType: UserType,
        roomId: String,
        myId: String,
        messageId: String?,
        unReadMessageCount: Int?,
    ): List<ChatMessage>

    suspend fun generateChatRooms(
        userType: UserType,
        opponentId: String,
    ): String

    suspend fun subscribeChatMessage(
        userId: String,
        userType: UserType,
    ): Flow<Message>

    suspend fun sendMessage(
        chatroomId: String,
        myId: String,
        receiverId: String,
        senderName: String,
        content: String,
        userType: UserType,
    )

    suspend fun readMessage(
        chatroomId: String,
        myId: String,
        opponentId: String,
        userType: UserType,
        sequence: Int,
    )
}
