package com.idle.domain.usecase.chat

import com.idle.domain.model.chat.ChatMessage
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor() {
    suspend operator fun invoke(roomId: String): Result<List<ChatMessage>> =
        Result.success(emptyList())
}
