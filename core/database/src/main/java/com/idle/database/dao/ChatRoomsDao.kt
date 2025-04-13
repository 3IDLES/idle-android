package com.idle.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.idle.database.model.ChatRoomEntity
import com.idle.database.model.ChatRoomWithMessages

@Dao
interface ChatRoomsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatRoom(chatRoom: ChatRoomEntity)

    @Transaction
    @Query(
        """
            SELECT * FROM chatRoom
            WHERE myId = :userId
            ORDER BY id ASC
        """
    )
    suspend fun getChatRoomsWithMessages(userId: String): List<ChatRoomWithMessages>

    @Query(
        """
            SELECT EXISTS(
                SELECT 1
                FROM chatRoom
                WHERE id = :roomId
            )
        """
    )
    suspend fun isChatRoomExist(roomId: String): Boolean
}
