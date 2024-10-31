package com.idle.data.repository.chatting

import com.idle.domain.repositorry.chatting.ChattingRepository
import com.idle.network.source.websocket.WebSocketDataSource
import javax.inject.Inject

class ChattingRepositoryImpl @Inject constructor(
    private val webSocketDataSource: WebSocketDataSource,
) : ChattingRepository {
    override suspend fun connectWebSocket(): Result<Unit> = webSocketDataSource.connectWebSocket()

    override suspend fun disconnectWebSocket(): Result<Unit> =
        webSocketDataSource.disconnectWebSocket()
}