package com.idle.network.model.chat

import com.idle.domain.model.chat.ChatRoom
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class GetChatRoomResponse(
    val chatRoomId: String = "",
    val lastMessage: String = "",
    val lastMessageTime: String?,
    val count: Int = 0,
    val myId: String = "",
    val opponentId: String = "",
    val opponentName: String = "",
    val opponentProfileImageUrl: String = "",
) {
    fun toVO() = ChatRoom(
        id = chatRoomId,
        lastMessage = lastMessage,
        lastMessageTime = lastMessageTime.let {
            LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
        } ?: LocalDateTime.MIN,
        unReadMessageCount = count,
        myId = myId,
        opponentName = opponentName,
        opponentId = opponentId,
        opponentProfileImageUrl = opponentProfileImageUrl,
    )
}
