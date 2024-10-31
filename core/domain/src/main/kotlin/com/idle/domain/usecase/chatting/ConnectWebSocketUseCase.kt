package com.idle.domain.usecase.chatting

import com.idle.domain.repositorry.chatting.ChattingRepository
import javax.inject.Inject

class ConnectWebSocketUseCase @Inject constructor(
    private val chattingRepository: ChattingRepository,
) {
    suspend operator fun invoke(): Result<Unit> =  chattingRepository.connectWebSocket()
}
