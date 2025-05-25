package com.idle.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.idle.database.model.MessageEntity

@Dao
interface MessagesDao {
    @Upsert
    suspend fun upsertMessage(messages: MessageEntity)

    @Query(
        """
            SELECT * FROM message
            WHERE roomId = :roomId
                AND myId = :myId
                AND (:lastMessageId IS NULL OR id < :lastMessageId)
            ORDER BY id DESC
            LIMIT :limit
        """
    )
    suspend fun getMessages(
        roomId: String,
        myId: String,
        lastMessageId: String?,
        limit: Int = 50,
    ): List<MessageEntity>

    @Query(
        """
            UPDATE message
            SET isRead = 1
            WHERE roomId = :roomId
                AND myId = :myId
                AND senderId = :opponentId
                AND isRead = 0
                AND sequence <= :sequence
        """
    )
    suspend fun readMessages(
        roomId: String,
        myId: String,
        opponentId: String,
        sequence: Int,
    )

    @Query(
        """
            SELECT EXISTS(
                SELECT 1
                FROM message
                WHERE roomId = :roomId
                AND myId = :myId
                AND id = :messageId
            )
        """
    )
    suspend fun isMessageExist(
        myId: String,
        roomId: String,
        messageId: String,
    ): Boolean

    @Query(
        """
        SELECT EXISTS(
            SELECT 1
            FROM message
            WHERE roomId = :roomId
              AND myId   = :myId
              AND sequence > :sequence
        )
        """
    )
    suspend fun hasMessagesAfterSequence(
        roomId: String,
        myId: String,
        sequence: Int
    ): Boolean
}
