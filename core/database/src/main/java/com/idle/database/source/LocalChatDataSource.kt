package com.idle.database.source

import com.idle.database.dao.ChatRoomsDao
import com.idle.database.dao.MessagesDao
import com.idle.database.model.ChatRoomEntity
import com.idle.database.model.ChatRoomWithMessages
import com.idle.database.model.MessageEntity
import com.idle.database.model.toMessageEntity
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.chat.ChatRoom
import javax.inject.Inject

class LocalChatDataSource @Inject constructor(
    private val messagesDao: MessagesDao,
    private val chatRoomsDao: ChatRoomsDao,
) {
    suspend fun insertMessage(message: ChatMessage, myId: String) =
        messagesDao.insertMessage(message.toMessageEntity(myId))

    suspend fun getMessages(
        roomId: String,
        lastMessageId: String?
    ): List<ChatMessage> =
        messagesDao.getMessages(
            roomId = roomId,
            lastMessageId = lastMessageId,
        ).map(MessageEntity::toDomain).reversed()

    suspend fun readMessages(
        roomId: String,
        opponentId: String,
    ): Unit = messagesDao.readMessages(roomId, opponentId)

    suspend fun isMessageExist(roomId: String, myId: String, messageId: String): Boolean =
        messagesDao.isMessageExist(
            roomId = roomId,
            myId = myId,
            messageId = messageId,
        )

    suspend fun insertChatRoomByWorker(myId: String, chatRoom: ChatRoom) =
        chatRoomsDao.insertChatRoom(
            ChatRoomEntity(
                id = chatRoom.id,
                opponentId = chatRoom.opponentId,
                myId = myId,
            )
        )

    suspend fun insertChatRoomByCenter(myId: String, chatRoom: ChatRoom) =
        chatRoomsDao.insertChatRoom(
            ChatRoomEntity(
                id = chatRoom.id,
                opponentId = myId,
                myId = chatRoom.opponentId,
            )
        )

    suspend fun isChatRoomExist(roomId: String, myId: String): Boolean =
        chatRoomsDao.isChatRoomExist(
            roomId = roomId,
            myId = myId,
        )

    suspend fun getChatRooms(userId: String): List<ChatRoom> =
        chatRoomsDao.getChatRoomsWithMessages(userId).map(ChatRoomWithMessages::toDomain)
}
