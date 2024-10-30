package com.idle.domain.usecase.chatting

import com.idle.domain.model.chatting.ChatMessage
import com.idle.domain.model.chatting.Content
import com.idle.domain.model.chatting.ContentType
import com.idle.domain.model.chatting.SenderType
import java.time.LocalDateTime
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor() {
    suspend operator fun invoke(roomId: String): Result<List<ChatMessage>> = Result.success(
        listOf(
            ChatMessage(
                id = "1",
                roomId = "room1",
                senderId = "user1",
                senderType = SenderType.USER,
                contents = listOf(
                    Content(
                        type = ContentType.TEXT,
                        value = "안녕하세요! 문의드리고 싶어서 연락드렸습니다."
                    )
                ),
                createdAt = LocalDateTime.now().minusDays(3)
            ),
            ChatMessage(
                id = "2",
                roomId = "room1",
                senderId = "user2",
                senderType = SenderType.USER,
                contents = listOf(Content(type = ContentType.TEXT, value = "안녕하세요! 어떤 문의사항이신가요?")),
                createdAt = LocalDateTime.now().minusDays(2)
            ),
            ChatMessage(
                id = "3",
                roomId = "room1",
                senderId = "user1",
                senderType = SenderType.USER,
                contents = listOf(Content(type = ContentType.TEXT, value = "추가로 여쭤보고 싶은 게 있습니다.")),
                createdAt = LocalDateTime.now().minusDays(1)
            ),
            ChatMessage(
                id = "4",
                roomId = "room1",
                senderId = "user2",
                senderType = SenderType.USER,
                contents = listOf(Content(type = ContentType.TEXT, value = "알겠습니다. 무엇이 궁금하신가요?")),
                createdAt = LocalDateTime.now().minusMinutes(3)
            ),
            ChatMessage(
                id = "5",
                roomId = "room1",
                senderId = "user1",
                senderType = SenderType.USER,
                contents = listOf(Content(type = ContentType.TEXT, value = "감사합니다! 친절한 답변 감사합니다.")),
                createdAt = LocalDateTime.now().minusMinutes(1)
            )
        )
    )
}