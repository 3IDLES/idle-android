package com.idle.domain.usecase.chat

import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.repositorry.chatting.ChatRepository
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(
        userType: UserType,
        roomId: String,
        messageId: String?,
    ): Result<List<ChatMessage>> =
        chatRepository.getChatRoomMessages(
            userType = userType,
            roomId = roomId,
            messageId = messageId,
        )
}
