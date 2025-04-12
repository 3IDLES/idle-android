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
    suspend fun insertMessages(message: ChatMessage) =
        messagesDao.insertMessage(message.let(ChatMessage::toMessageEntity))

    suspend fun getMessages(
        roomId: String,
        lastMessageId: String?
    ): List<ChatMessage> =
        messagesDao.getMessages(
            roomId = roomId,
            lastMessageId = lastMessageId,
        ).map(MessageEntity::toDomain)
            .asReversed()

    suspend fun insertChatRoom(myId: String, chatRoom: ChatRoom) = chatRoomsDao.insertChatRoom(
        ChatRoomEntity(
            id = chatRoom.id,
            opponentId = chatRoom.opponentId,
            myId = myId,
        )
    )

    suspend fun isChatRoomExist(roomId: String): Boolean = chatRoomsDao.isChatRoomExists(roomId)

    suspend fun getChatRooms(userId: String): List<ChatRoom> =
        chatRoomsDao.getChatRoomsWithMessages(userId)
            .map(ChatRoomWithMessages::toDomain)
}
