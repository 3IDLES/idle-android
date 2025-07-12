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
    override suspend fun connectWebSocket() {
        chatDataSource.connectWebSocket()
    }

    override suspend fun disconnectWebSocket() {
        chatDataSource.disconnectWebSocket()
    }

    override suspend fun retrieveChatRooms(userId: String): List<ChatRoom> {
        return localChatDataSource.getChatRooms(userId)
    }

    override suspend fun loadChatRooms(
        userId: String,
        userType: UserType
    ): List<ChatRoomWithOpponentInfo> {
        val response = if (userType == UserType.WORKER) {
            chatDataSource.getWorkerChatRooms()
        } else {
            chatDataSource.getCenterChatRooms()
        }

        return response.map { dto ->
            val chatRoom = dto.toVO()
            if (!localChatDataSource.isChatRoomExist(chatRoom.id, userId)) {
                localChatDataSource.insertChatRoom(
                    myId = userId,
                    chatRoom = ChatRoom(
                        id = chatRoom.id,
                        opponentId = chatRoom.opponentId,
                        lastMessage = chatRoom.lastMessage,
                        lastMessageTime = chatRoom.lastMessageTime,
                        unReadMessageCount = chatRoom.unReadMessageCount
                    )
                )
            }
            chatRoom
        }
    }

    override suspend fun retrieveChatRoomMessages(
        roomId: String,
        myId: String,
        messageId: String?
    ): List<ChatMessage> {
        return localChatDataSource.getMessages(
            roomId = roomId,
            myId = myId,
            lastMessageId = messageId
        )
    }

    override suspend fun getChatRoomMessages(
        userType: UserType,
        roomId: String,
        myId: String,
        messageId: String?,
        unReadMessageCount: Int?
    ): List<ChatMessage> {
        if (messageId == null || !localChatDataSource.isMessageExist(roomId, myId, messageId)) {
            val response = if (userType == UserType.WORKER) {
                chatDataSource.getWorkerChatRoomMessages(roomId, messageId)
            } else {
                chatDataSource.getCenterChatRoomMessages(roomId, messageId)
            }

            val allMessages = response.chatMessageInfos
                .sortedBy { it.sequence }
                .map { it.toVO() }

            val messagesToUse = unReadMessageCount?.let { count ->
                allMessages.takeLast(count)
            } ?: allMessages

            val maxSeq = localChatDataSource.getMaxLocalSequence(roomId, myId) ?: Int.MIN_VALUE

            messagesToUse.forEach { message ->
                if (!localChatDataSource.isChatRoomExist(message.roomId, myId)) {
                    localChatDataSource.insertChatRoom(
                        myId = myId,
                        chatRoom = ChatRoom(
                            id = message.roomId,
                            opponentId = if (myId == message.senderId) message.receiverId else message.senderId,
                            lastMessage = message.content,
                            lastMessageTime = message.createdAt,
                            unReadMessageCount = 1
                        )
                    )
                }

                if (message.sequence > maxSeq) {
                    localChatDataSource.insertMessage(message, myId)
                }
            }

            response.sequence.takeIf { it >= 0 }?.let { seq ->
                allMessages.lastOrNull()?.let {
                    localChatDataSource.readMessages(
                        roomId = roomId,
                        myId = myId,
                        senderId = it.senderId,
                        sequence = seq
                    )
                }
            }
        }

        return localChatDataSource.getMessages(
            roomId = roomId,
            myId = myId,
            lastMessageId = messageId
        )
    }

    override suspend fun generateChatRooms(
        userType: UserType,
        opponentId: String
    ): String {
        val dto = if (userType == UserType.WORKER) {
            chatDataSource.generateWorkerChatRoom(opponentId)
        } else {
            chatDataSource.generateCenterChatRoom(opponentId)
        }
        return dto.toVO()
    }

    override suspend fun subscribeChatMessage(userId: String, userType: UserType): Flow<Message> {
        return chatDataSource.subscribeChatMessage(userId)
            .map { response ->
                val message = response.toVO()
                when (message) {
                    is ChatMessage -> {
                        if (!localChatDataSource.isChatRoomExist(
                                roomId = message.roomId,
                                myId = userId
                            )
                        ) {
                            localChatDataSource.insertChatRoom(
                                myId = userId,
                                chatRoom = ChatRoom(
                                    id = message.roomId,
                                    opponentId = if (userId == message.senderId) message.receiverId else message.senderId,
                                    lastMessage = message.content,
                                    lastMessageTime = message.createdAt,
                                    unReadMessageCount = 1
                                )
                            )
                        }
                        localChatDataSource.insertMessage(message, userId)
                    }
                    is ReadMessage -> {
                        if (message.opponentId != userId) {
                            localChatDataSource.readMessages(
                                roomId = message.chatroomId,
                                myId = userId,
                                senderId = message.opponentId,
                                sequence = message.sequence
                            )
                        }
                    }
                }
                message
            }
            .retryWhen { cause, attempt ->
                if (cause is IOException && attempt < MAX_RETRY_ATTEMPTS) {
                    connectWebSocket()
                    delay(calculateBackoffTime(attempt.toInt()))
                    true
                } else {
                    false
                }
            }
    }

    override suspend fun sendMessage(
        chatroomId: String,
        myId: String,
        receiverId: String,
        senderName: String,
        content: String,
        userType: UserType
    ) {
        chatDataSource.sendMessage(
            userType = userType,
            sendMessageRequest = SendMessageRequest(
                chatroomId = chatroomId,
                receiverId = receiverId,
                senderName = senderName,
                content = content
            )
        )
    }

    override suspend fun readMessage(
        chatroomId: String,
        myId: String,
        opponentId: String,
        userType: UserType,
        sequence: Int
    ) {
        chatDataSource.readMessage(
            userType = userType,
            readMessageRequest = ReadMessageRequest(
                chatroomId = chatroomId,
                opponentId = opponentId,
                sequence = sequence
            )
        )
        localChatDataSource.readMessages(
            roomId = chatroomId,
            myId = myId,
            senderId = opponentId,
            sequence = sequence
        )
    }
}
