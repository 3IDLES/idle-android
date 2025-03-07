package com.idle.domain.usecase.chat

import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.repositorry.chatting.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeChatMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(userId: String): Flow<ChatMessage> {
        return chatRepository.subscribeChatMessage(userId)
    }
}
