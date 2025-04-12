package com.idle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.idle.domain.model.chat.ChatMessage
import java.time.LocalDateTime

@Entity(
    tableName = "message",
    indices = [Index(value = ["roomId", "id"], unique = false)],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = ChatRoomEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("roomId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    )
)
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
    internal fun toDomain() = ChatMessage(
        id = id,
        roomId = roomId,
        senderId = senderId,
        receiverId = receiverId,
        content = content,
        createdAt = createdAt,
        isRead = isRead,
    )
}

internal fun ChatMessage.toMessageEntity() = MessageEntity(
    id = id,
    roomId = roomId,
    senderId = senderId,
    receiverId = receiverId,
    content = content,
    createdAt = createdAt,
    isRead = isRead,
)
