package com.idle.domain.usecase.chatting

import com.idle.domain.repositorry.chatting.ChatRepository
import javax.inject.Inject

class ConnectWebSocketUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(): Result<Unit> =  chatRepository.connectWebSocket()
}
