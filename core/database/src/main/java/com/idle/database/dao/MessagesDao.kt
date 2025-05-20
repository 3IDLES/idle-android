package com.idle.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.idle.database.model.MessageEntity

@Dao
interface MessagesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(messages: MessageEntity)

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
        limit: Int = 50
    ): List<MessageEntity>

    @Query(
        """
            UPDATE message
            SET isRead = 1
            WHERE roomId = :roomId
                AND myId = :myId
                AND senderId = :opponentId
                AND isRead = 0
        """
    )
    suspend fun readMessages(
        roomId: String,
        myId: String,
        opponentId: String,
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
}
