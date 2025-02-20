package com.idle.domain.usecase.chat

import com.idle.domain.repositorry.chatting.ChatRepository
import javax.inject.Inject

class DisconnectWebSocketUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke() = chatRepository.disconnectWebSocket()
}
