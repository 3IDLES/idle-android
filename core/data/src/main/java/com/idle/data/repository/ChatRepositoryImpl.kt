package com.idle.data.repository

import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.chat.ChatRoom
import com.idle.domain.repositorry.ChatRepository
import com.idle.network.model.chat.ReadMessageRequest
import com.idle.network.model.chat.SendMessageRequest
import com.idle.network.source.ChatDataSource
import com.idle.network.util.MAX_RETRY_ATTEMPTS
import com.idle.network.util.calculateBackoffTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDataSource: ChatDataSource,
) : ChatRepository {
    override suspend fun connectWebSocket(): Result<Unit> = chatDataSource.connectWebSocket()

    override suspend fun disconnectWebSocket(): Result<Unit> =
        chatDataSource.disconnectWebSocket()

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

    override suspend fun subscribeChatMessage(userId: String): Flow<ChatMessage> =
        chatDataSource.subscribeChatMessage(userId)
            .map { it.toVO() }
            .retryWhen { cause, attempt ->
                if (cause is IOException && attempt < MAX_RETRY_ATTEMPTS) {
                    connectWebSocket()
                    delay(calculateBackoffTime(attempt.toInt()))
                    true
                } else {
                    false
                }
            }

    override suspend fun sendMessage(
        chatroomId: String,
        receiverId: String,
        senderName: String,
        content: String,
    ): Result<Unit> = chatDataSource.sendMessage(
        SendMessageRequest(
            chatroomId = chatroomId,
            receiverId = receiverId,
            senderName = senderName,
            content = content,
        )
    )

    override suspend fun readMessage(chatroomId: String, opponentId: String): Result<Unit> =
        chatDataSource.readMessage(
            ReadMessageRequest(
                chatroomId = chatroomId,
                opponentId = opponentId,
            )
        )
}
