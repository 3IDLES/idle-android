package com.idle.data.repository.chat

import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.chat.ChatRoom
import com.idle.domain.repositorry.chatting.ChatRepository
import com.idle.network.source.chat.ChatDataSource
import com.idle.network.source.websocket.WebSocketDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val webSocketDataSource: WebSocketDataSource,
    private val chatDataSource: ChatDataSource,
) : ChatRepository {
    override suspend fun connectWebSocket(): Result<Unit> = webSocketDataSource.connectWebSocket()

    override suspend fun disconnectWebSocket(): Result<Unit> =
        webSocketDataSource.disconnectWebSocket()

    override suspend fun getChatRooms(userType: UserType): Result<List<ChatRoom>> =
        runCatching {
            val chatRoomsResponse = when (userType) {
                UserType.WORKER -> chatDataSource.getWorkerChatRooms()
                UserType.CENTER -> chatDataSource.getCenterChatRooms()
            }.getOrThrow()

            chatRoomsResponse.map { it.toVO() }
        }

    override suspend fun getChatRoomMessages(
        userType: UserType,
        roomId: String,
        messageId: String?,
    ): Result<List<ChatMessage>> = runCatching {
        when (userType) {
            UserType.WORKER -> chatDataSource.getWorkerChatRoomMessages(
                roomId = roomId,
                messageId = messageId,
            )

            UserType.CENTER -> chatDataSource.getCenterChatRoomMessages(
                roomId = roomId,
                messageId = messageId,
            )
        }.mapCatching { messages -> messages.map { it.toVO() } }
            .getOrThrow()
    }

    override suspend fun generateChatRooms(userType: UserType, opponentId: String): Result<String> =
        runCatching {
            when (userType) {
                UserType.WORKER -> chatDataSource.generateWorkerChatRoom(opponentId)
                UserType.CENTER -> chatDataSource.generateCenterChatRoom(opponentId)
            }.mapCatching { it.toVO() }
                .getOrThrow()
        }

    override fun subscribeChatMessage(): Flow<ChatMessage> =
        webSocketDataSource.chatMessageFlow
            .filterNotNull()
            .map { it.toVO() }
}
