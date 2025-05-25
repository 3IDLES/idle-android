package com.idle.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.idle.domain.model.chat.ChatMessage
import java.time.LocalDateTime

@Entity(
    tableName = "message",
    indices = [Index(value = ["roomId", "id", "sequence"], unique = true)],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = ChatRoomEntity::class,
            parentColumns = ["id", "myId"],
            childColumns = ["roomId", "myId"],
        )
    )
)
data class MessageEntity(
    @PrimaryKey val id: String,
    val roomId: String,
    val myId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val createdAt: LocalDateTime,
    val isRead: Boolean,
    val sequence: Int,
) {
    internal fun toDomain() = ChatMessage(
        id = id,
        roomId = roomId,
        senderId = senderId,
        receiverId = receiverId,
        content = content,
        createdAt = createdAt,
        isRead = isRead,
        sequence = sequence,
    )
}

internal fun ChatMessage.toMessageEntity(myId: String) = MessageEntity(
    id = id,
    myId = myId,
    roomId = roomId,
    senderId = senderId,
    receiverId = receiverId,
    content = content,
    createdAt = createdAt,
    isRead = isRead,
    sequence = sequence,
)
