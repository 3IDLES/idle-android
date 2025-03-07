package com.idle.network.api.websocket

import com.idle.network.BuildConfig
import com.idle.network.model.chat.ChatMessageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow

@Singleton
class WebSocketDataSource @Inject constructor(
    private val client: StompClient,
    private val json: Json,
) {
    private var session: StompSession? = null
    private var connectionAttempts = 0

    /**
     *     STOMP 연결이 안되는 상황.
     *     서버 응답 기다려야 재개 가능~
     */

    suspend fun connectWebSocket(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            session = client.connect(
                "${BuildConfig.CARE_WEBSOCKET_URL}/ws",
            ).withJsonConversions(json)

            connectionAttempts = 0
            Result.success(Unit)
        } catch (e: Exception) {
            if (connectionAttempts < MAX_RETRY_ATTEMPTS) {
                val waitTime = minOf(calculateBackoffTime(connectionAttempts), MAX_WAIT_TIME)
                delay(waitTime)
                connectionAttempts++
                connectWebSocket()
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun disconnectWebSocket(reason: String? = null): Result<Unit> {
        return try {
            session?.disconnect()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun subscribeChatMessage(userId: String): Flow<ChatMessageResponse> =
        session?.subscribe(StompSubscribeHeaders(destination = "/sub/${userId}"))
            ?.map { json.decodeFromString<ChatMessageResponse>(it.bodyAsText) }
            ?: flow { throw IllegalStateException("웹소켓을 먼저 연결해주세요.") }

    private fun calculateBackoffTime(attempt: Int): Long = (2.0.pow(attempt) * 1000).toLong()

    companion object {
        private const val MAX_RETRY_ATTEMPTS = 5
        private const val MAX_WAIT_TIME = 10_000L // 10 seconds
    }
}
