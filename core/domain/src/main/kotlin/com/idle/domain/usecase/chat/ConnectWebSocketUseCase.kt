package com.idle.domain.usecase.chat

import com.idle.domain.repositorry.chatting.ChatRepository
import javax.inject.Inject

class ConnectWebSocketUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(): Result<Unit> =  chatRepository.connectWebSocket()
}
