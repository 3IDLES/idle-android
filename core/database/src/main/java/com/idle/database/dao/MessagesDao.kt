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
            AND (:lastMessageId IS NULL OR id < :lastMessageId)
            ORDER BY id DESC
            LIMIT :limit
        """
    )
    suspend fun getMessages(
        roomId: String,
        lastMessageId: String?,
        limit: Int = 50
    ): List<MessageEntity>
}
