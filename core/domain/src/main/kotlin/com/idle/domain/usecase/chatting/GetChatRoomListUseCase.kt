package com.idle.domain.usecase.chatting

import com.idle.domain.model.chatting.ChatRoom
import java.time.LocalDateTime
import javax.inject.Inject

class GetChatRoomListUseCase @Inject constructor(
) {
    suspend operator fun invoke(): Result<List<ChatRoom>> = Result.success(
        listOf(
            ChatRoom(
                id = "1",
                sender = "user1",
                receiver = "user2",
                createdAt = LocalDateTime.now().minusDays(1),
                lastMessage = "안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!",
                lastSentAt = LocalDateTime.now().minusHours(1),
                unReadMessageCount = 99,
                profileImageUrl = "",
            ),
            ChatRoom(
                id = "2",
                sender = "user3",
                receiver = "user4",
                createdAt = LocalDateTime.now().minusDays(3),
                lastMessage = "오늘 만날 수 있을까요?",
                lastSentAt = LocalDateTime.now().minusHours(5),
                unReadMessageCount = 100,
                profileImageUrl = "",
            ),
            ChatRoom(
                id = "3",
                sender = "user5",
                receiver = "user6",
                createdAt = LocalDateTime.now().minusDays(7),
                lastMessage = "네, 좋습니다!",
                lastSentAt = LocalDateTime.now().minusDays(1).minusHours(2),
                unReadMessageCount = 1,
                profileImageUrl = "",
            ),
            ChatRoom(
                id = "4",
                sender = "user7user7user7user7user7user7user7user7user7user7user7user7",
                receiver = "user8",
                createdAt = LocalDateTime.now().minusHours(2),
                lastMessage = "곧 출발합니다.곧 출발합니다.곧 출발합니다.곧 출발합니다.곧 출발합니다.곧 출발합니다.곧 출발합니다.곧 출발합니다.",
                lastSentAt = LocalDateTime.now().minusMinutes(30),
                unReadMessageCount = 0,
                profileImageUrl = "",
            ),
            ChatRoom(
                id = "5",
                sender = "user9",
                receiver = "user10",
                createdAt = LocalDateTime.now().minusWeeks(1),
                lastMessage = "다음 주에 봐요!",
                lastSentAt = LocalDateTime.now().minusDays(3),
                unReadMessageCount = 0,
                profileImageUrl = "https://cdn.pixabay.com/photo/2020/05/17/20/21/cat-5183427_1280.jpg",
            )
        )
    )
}