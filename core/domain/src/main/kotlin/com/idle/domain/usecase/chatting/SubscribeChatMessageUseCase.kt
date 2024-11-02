package com.idle.domain.usecase.chatting

import com.idle.domain.model.chatting.ChatMessage
import com.idle.domain.repositorry.chatting.ChattingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeChatMessageUseCase @Inject constructor(
    private val chattingRepository: ChattingRepository,
) {
    operator fun invoke(): Flow<ChatMessage> = chattingRepository.subscribeChatMessage()
}