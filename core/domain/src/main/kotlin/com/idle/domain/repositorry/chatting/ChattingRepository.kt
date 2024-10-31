package com.idle.domain.repositorry.chatting

interface ChattingRepository {
    suspend fun connectWebSocket(): Result<Unit>
    suspend fun disconnectWebSocket(): Result<Unit>
}
