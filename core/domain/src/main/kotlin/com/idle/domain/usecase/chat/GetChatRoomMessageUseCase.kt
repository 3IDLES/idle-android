package com.idle.domain.usecase.chat

import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.repositorry.ChatRepository
import javax.inject.Inject

class GetChatRoomMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(
        userType: UserType,
        roomId: String,
        messageId: String?,
    ): Result<List<ChatMessage>> = chatRepository.getChatRoomMessages(
        userType = userType,
        roomId = roomId,
        messageId = messageId,
    ).map { messages -> messages.reversed() }
}
