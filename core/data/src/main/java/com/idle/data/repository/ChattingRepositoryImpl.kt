package com.idle.data.repository

import com.idle.domain.model.chatting.ChatMessage
import com.idle.domain.repositorry.ChattingRepository
import com.idle.network.source.websocket.WebSocketDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChattingRepositoryImpl @Inject constructor(
    private val webSocketDataSource: WebSocketDataSource,
) : ChattingRepository {
    override suspend fun connectWebSocket(): Result<Unit> = webSocketDataSource.connectWebSocket()

    override suspend fun disconnectWebSocket(): Result<Unit> =
        webSocketDataSource.disconnectWebSocket()

    override fun subscribeChatMessage(): Flow<ChatMessage> =
        webSocketDataSource.chatMessageFlow
            .filterNotNull()
            .map { it.toVO() }
}
