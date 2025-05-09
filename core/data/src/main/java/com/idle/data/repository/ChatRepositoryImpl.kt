package com.idle.data.repository

import com.idle.database.source.LocalChatDataSource
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.chat.ChatRoom
import com.idle.domain.model.chat.ChatRoomWithOpponentInfo
import com.idle.domain.model.chat.Message
import com.idle.domain.model.chat.ReadMessage
import com.idle.domain.repositorry.ChatRepository
import com.idle.network.model.chat.ReadMessageRequest
import com.idle.network.model.chat.SendMessageRequest
import com.idle.network.source.ChatDataSource
import com.idle.network.util.MAX_RETRY_ATTEMPTS
import com.idle.network.util.calculateEqualJitter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDataSource: ChatDataSource,
    private val localChatDataSource: LocalChatDataSource,
) : ChatRepository {
    override suspend fun connectWebSocket(): Result<Unit> = chatDataSource.connectWebSocket()

    override suspend fun disconnectWebSocket(): Result<Unit> =
        chatDataSource.disconnectWebSocket()

    override suspend fun retrieveChatRooms(userId: String): Result<List<ChatRoom>> = runCatching {
        localChatDataSource.getChatRooms(userId)
    }

    override suspend fun loadChatRooms(
        userId: String,
        userType: UserType
    ): Result<List<ChatRoomWithOpponentInfo>> = runCatching {
        val chatRoomsResponse = when (userType) {
            UserType.WORKER -> chatDataSource.getWorkerChatRooms()
            UserType.CENTER -> chatDataSource.getCenterChatRooms()
        }.getOrThrow()
        chatRoomsResponse.map {
            it.toVO()
        }
    }

    override suspend fun retrieveChatRoomMessages(
        roomId: String,
        messageId: String?
    ): Result<List<ChatMessage>> = runCatching {
        localChatDataSource.getMessages(
            roomId = roomId,
            lastMessageId = messageId,
        )
    }

    override suspend fun getChatRoomMessages(
        userType: UserType,
        roomId: String,
        myId: String,
        messageId: String?,
    ): Result<List<ChatMessage>> = runCatching {
        if (messageId == null ||
            !localChatDataSource.isMessageExist(roomId = roomId, messageId = messageId)
        ) {
            // 웹소켓으로 얻지 못한 메세지가 있을경우 정합성을 위해 서버에서 호출
            when (userType) {
                UserType.WORKER -> chatDataSource.getWorkerChatRoomMessages(
                    roomId = roomId,
                    messageId = messageId,
                )

                UserType.CENTER -> chatDataSource.getCenterChatRoomMessages(
                    roomId = roomId,
                    messageId = messageId,
                )
            }.mapCatching { response ->
                response.map {
                    val message = it.toVO()
                    if (!localChatDataSource.isChatRoomExist(message.roomId)) {
                        localChatDataSource.insertChatRoom(
                            myId = myId,
                            chatRoom = ChatRoom(
                                id = message.roomId,
                                opponentId = if (myId == message.senderId) message.receiverId else message.senderId,
                                lastMessage = message.content,
                                lastMessageTime = message.createdAt,
                                unReadMessageCount = 1,
                            )
                        )
                    }
                    localChatDataSource.insertMessage(message)
                }
            }.getOrThrow()
        }

        return@runCatching localChatDataSource.getMessages(
            roomId = roomId,
            lastMessageId = messageId,
        )
    }

    override suspend fun generateChatRooms(
        userType: UserType,
        opponentId: String
    ): Result<String> =
        runCatching {
            when (userType) {
                UserType.WORKER -> chatDataSource.generateWorkerChatRoom(opponentId)
                UserType.CENTER -> chatDataSource.generateCenterChatRoom(opponentId)
            }.mapCatching {
                it.toVO()
            }.getOrThrow()
        }

    override suspend fun subscribeChatMessage(userId: String): Flow<Message> =
        chatDataSource.subscribeChatMessage(userId)
            .map {
                val message = it.toVO()
                when (message) {
                    is ChatMessage -> {
                        if (!localChatDataSource.isChatRoomExist(message.roomId)) {
                            localChatDataSource.insertChatRoom(
                                myId = userId,
                                chatRoom = ChatRoom(
                                    id = message.roomId,
                                    opponentId = if (userId == message.senderId) message.receiverId else message.senderId,
                                    lastMessage = message.content,
                                    lastMessageTime = message.createdAt,
                                    unReadMessageCount = 1,
                                )
                            )
                        }

                        localChatDataSource.insertMessage(message)
                    }

                    is ReadMessage -> {
                        localChatDataSource.readMessages(
                            roomId = message.chatroomId,
                            opponentId = userId,
                        )
                    }
                }
                message
            }.retryWhen { cause, attempt ->
                if (cause is IOException && attempt < MAX_RETRY_ATTEMPTS) {
                    connectWebSocket()
                    delay(calculateEqualJitter(attempt.toInt()))
                    true
                } else {
                    false
                }
            }

    override suspend fun sendMessage(
        chatroomId: String,
        myId: String,
        receiverId: String,
        senderName: String,
        content: String,
        userType: UserType,
    ): Result<Unit> = chatDataSource.sendMessage(
        userType = userType,
        sendMessageRequest = SendMessageRequest(
            chatroomId = chatroomId,
            receiverId = receiverId,
            senderName = senderName,
            content = content,
        )
    )

    override suspend fun readMessage(
        chatroomId: String,
        opponentId: String,
        userType: UserType,
    ): Result<Unit> =
        chatDataSource.readMessage(
            userType = userType,
            readMessageRequest = ReadMessageRequest(
                chatroomId = chatroomId,
                opponentId = opponentId,
            )
        ).onSuccess {
            localChatDataSource.readMessages(
                roomId = chatroomId,
                opponentId = opponentId,
            )
        }
}
