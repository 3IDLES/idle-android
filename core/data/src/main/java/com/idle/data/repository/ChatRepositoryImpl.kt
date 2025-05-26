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
import com.idle.network.util.calculateBackoffTime
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

        val chatRooms = chatRoomsResponse.map {
            val chatRoom = it.toVO()

            if (!localChatDataSource.isChatRoomExist(chatRoom.id, userId)) {
                localChatDataSource.insertChatRoom(
                    myId = userId,
                    chatRoom = ChatRoom(
                        id = chatRoom.id,
                        opponentId = chatRoom.opponentId,
                        lastMessage = chatRoom.lastMessage,
                        lastMessageTime = chatRoom.lastMessageTime,
                        unReadMessageCount = chatRoom.unReadMessageCount,
                    )
                )
            }
            chatRoom
        }
        chatRooms
    }

    override suspend fun retrieveChatRoomMessages(
        roomId: String,
        myId: String,
        messageId: String?
    ): Result<List<ChatMessage>> = runCatching {
        localChatDataSource.getMessages(
            roomId = roomId,
            myId = myId,
            lastMessageId = messageId,
        )
    }

    override suspend fun getChatRoomMessages(
        userType: UserType,
        roomId: String,
        myId: String,
        messageId: String?,
    ): Result<List<ChatMessage>> = runCatching {
        if (messageId == null || !localChatDataSource.isMessageExist(roomId, myId, messageId)) {
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
                val messages = response.chatMessageInfos

                messages
                    .sortedBy { it.sequence }
                    .forEach {
                        val message = it.toVO()

                        if (!localChatDataSource.isChatRoomExist(message.roomId, myId)) {
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

                        val maxSeq =
                            localChatDataSource.getMaxLocalSequence(roomId, myId) ?: Int.MIN_VALUE
                        if (message.sequence > maxSeq) {
                            localChatDataSource.insertMessage(message, myId)
                        }
                    }

                val readSequence = response.sequence
                messages.lastOrNull()?.let {
                    localChatDataSource.readMessages(
                        roomId = roomId,
                        myId = myId,
                        senderId = it.senderId ?: return@let,
                        sequence = readSequence,
                    )
                }

                messages
            }.getOrThrow()
        }

        return@runCatching localChatDataSource.getMessages(
            roomId = roomId,
            myId = myId,
            lastMessageId = messageId,
        )
    }

    override suspend fun generateChatRooms(
        userType: UserType,
        opponentId: String,
    ): Result<String> =
        runCatching {
            when (userType) {
                UserType.WORKER -> chatDataSource.generateWorkerChatRoom(opponentId)
                UserType.CENTER -> chatDataSource.generateCenterChatRoom(opponentId)
            }.mapCatching {
                it.toVO()
            }.getOrThrow()
        }

    override suspend fun subscribeChatMessage(userId: String, userType: UserType): Flow<Message> =
        chatDataSource.subscribeChatMessage(userId)
            .map {
                val message = it.toVO()

                when (message) {
                    is ChatMessage -> {
                        if (!localChatDataSource.isChatRoomExist(
                                roomId = message.roomId,
                                myId = userId,
                            )
                        ) {
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

                        localChatDataSource.insertMessage(message, userId)
                    }

                    is ReadMessage -> {
                        localChatDataSource.readMessages(
                            roomId = message.chatroomId,
                            myId = userId,
                            senderId = message.opponentId,
                            sequence = message.sequence,
                        )
                    }
                }

                message
            }.retryWhen { cause, attempt ->
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
        myId: String,
        opponentId: String,
        userType: UserType,
        sequence: Int,
    ): Result<Unit> =
        chatDataSource.readMessage(
            userType = userType,
            readMessageRequest = ReadMessageRequest(
                chatroomId = chatroomId,
                opponentId = opponentId,
                sequence = sequence,
            )
        ).onSuccess {
            localChatDataSource.readMessages(
                roomId = chatroomId,
                myId = myId,
                senderId = opponentId,
                sequence = sequence,
            )
        }
}
