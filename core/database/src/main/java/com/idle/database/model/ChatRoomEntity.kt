package com.idle.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.idle.domain.model.chat.ChatRoom
import java.time.LocalDateTime

@Entity(
    tableName = "chatRoom",
    indices = [Index(value = ["id"], unique = false)],
)
data class ChatRoomEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    val opponentId: String,
    val myId: String,
)

data class ChatRoomWithMessages(
    @Embedded
    val chatRoom: ChatRoomEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "roomId",
    )
    val messages: List<MessageEntity>
) {
    fun toDomain() = ChatRoom(
        id = chatRoom.id,
        opponentId = chatRoom.opponentId,
        lastMessage = messages.lastOrNull()?.content ?: "",
        lastMessageTime = messages.lastOrNull()?.createdAt ?: LocalDateTime.now(),
        unReadMessageCount = messages.filter { message ->
            message.receiverId == chatRoom.myId
        }.count { message ->
            !message.isRead
        },
    )
}
