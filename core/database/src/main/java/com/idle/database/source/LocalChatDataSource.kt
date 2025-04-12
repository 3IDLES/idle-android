package com.idle.database.source

import com.idle.database.dao.MessagesDao
import com.idle.database.model.MessageEntity
import com.idle.database.model.toMessageEntity
import com.idle.domain.model.chat.ChatMessage
import javax.inject.Inject

class LocalChatDataSource @Inject constructor(
    private val messagesDao: MessagesDao,
) {
    suspend fun insertMessages(message: ChatMessage) =
        messagesDao.insertMessages(message.let(ChatMessage::toMessageEntity))

    suspend fun getMessages(roomId: String, lastMessageId: String?): List<ChatMessage> =
        messagesDao.getMessages(roomId, lastMessageId).map(MessageEntity::toDomain)
            .asReversed()
}
