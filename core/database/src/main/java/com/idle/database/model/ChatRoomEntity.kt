package com.idle.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Relation
import com.idle.domain.model.chat.ChatRoom
import java.time.LocalDateTime

@Entity(
    tableName = "chatRoom",
    primaryKeys = ["id", "myId"],
    indices = [Index(value = ["id"])],
)
data class ChatRoomEntity(
    val id: String,
    val myId: String,
    val opponentId: String,
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
