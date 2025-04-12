package com.idle.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.idle.database.model.MessageEntity

@Dao
interface MessagesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(vararg messages: MessageEntity)

    @Query(value = "SELECT * FROM message")
    suspend fun getMessages(): List<MessageEntity>

    @Query(value = "DELETE FROM message")
    suspend fun clearMessages()
}
