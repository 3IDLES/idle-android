package com.idle.network.api.websocket

import com.idle.network.BuildConfig
import com.idle.network.model.chat.ChatMessageResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import okio.IOException
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketDataSource @Inject constructor(
    private val client: StompClient,
    private val json: Json,
) {
    private var session: StompSession? = null
    private var connectionAttempts = 0

    suspend fun connectWebSocket(): Result<Unit> = try {
        session = client.connect(BuildConfig.CARE_WEBSOCKET_URL)
            .withJsonConversions(json)

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

    suspend fun disconnectWebSocket(reason: String? = null): Result<Unit> = try {
        session?.disconnect()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun subscribeChatMessage(userId: String): Flow<ChatMessageResponse> =
        session?.subscribe(StompSubscribeHeaders(destination = "/sub/${userId}"))
            ?.map { json.decodeFromString<ChatMessageResponse>(it.bodyAsText) }
            ?: flow { throw IOException("웹소켓을 먼저 연결해주세요.") }
}
