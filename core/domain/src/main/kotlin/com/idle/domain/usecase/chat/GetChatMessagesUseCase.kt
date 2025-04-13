package com.idle.domain.usecase.chat

import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.repositorry.ChatRepository
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(
        userType: UserType,
        roomId: String,
        messageId: String?,
    ): Result<List<ChatMessage>> {
        val localResult = chatRepository.retrieveChatRoomMessages(
            roomId = roomId,
            messageId = messageId
        )

        val localMessages = localResult.getOrElse {
            return fetchFromServer(userType, roomId, messageId)
        }

        // 만약 로컬 데이터 값이 비었다면 서버 데이터 호출
        return if (localMessages.isEmpty()) {
            fetchFromServer(userType, roomId, messageId)
        } else {
            Result.success(localMessages)
        }
    }

    private suspend fun fetchFromServer(
        userType: UserType,
        roomId: String,
        messageId: String?
    ): Result<List<ChatMessage>> {
        return chatRepository.getChatRoomMessages(
            userType = userType,
            roomId = roomId,
            messageId = messageId
        )
    }
}

