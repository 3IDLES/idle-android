package com.idle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idle.domain.model.chat.ChatMessage
import java.time.LocalDateTime

@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    val roomId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val createdAt: LocalDateTime,
    val isRead: Boolean,
) {
    fun toDomain() = ChatMessage(
        id = id,
        roomId = roomId,
        senderId = senderId,
        receiverId = receiverId,
        content = content,
        createdAt = createdAt,
        isRead = isRead,
    )
}
