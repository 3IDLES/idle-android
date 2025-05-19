package com.idle.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.idle.database.model.ChatRoomEntity
import com.idle.database.model.ChatRoomWithMessages
import com.idle.database.model.MessageEntity

@Dao
interface ChatRoomsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatRoom(chatRoom: ChatRoomEntity)

    @Query(
        """
            SELECT *
            FROM chatRoom
            WHERE myId = :userId
            ORDER BY id ASC
        """
    )
    suspend fun getChatRooms(userId: String): List<ChatRoomEntity>

    @Query(
        """
            SELECT *
            FROM message
            WHERE myId = :userId
            ORDER BY createdAt ASC
        """
    )
    suspend fun getUserMessages(userId: String): List<MessageEntity>

    @Transaction
    suspend fun getChatRoomsWithMessages(userId: String): List<ChatRoomWithMessages> {
        val rooms    = getChatRooms(userId)
        val messages = getUserMessages(userId)
            .groupBy { it.roomId }

        return rooms.map { room ->
            ChatRoomWithMessages(
                chatRoom = room,
                messages = messages[room.id] ?: emptyList()
            )
        }
    }

    @Query(
        """
            SELECT EXISTS(
                SELECT 1
                FROM chatRoom
                WHERE id = :roomId
                AND myId = :myId
            )
        """
    )
    suspend fun isChatRoomExist(roomId: String, myId: String): Boolean
}
