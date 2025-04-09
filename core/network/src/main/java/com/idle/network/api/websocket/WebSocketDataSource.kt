package com.idle.network.api.websocket

import com.idle.network.BuildConfig
import com.idle.network.di.TokenManager
import com.idle.network.model.chat.ChatMessageResponse
import com.idle.network.model.chat.SendChatMessageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import okio.IOException
import org.hildan.krossbow.stomp.config.StompConfig
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.stomp.stomp
import org.hildan.krossbow.websocket.WebSocketClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketDataSource @Inject constructor(
    private val client: WebSocketClient,
    private val tokenManager: TokenManager,
    private val json: Json,
) {
    private var session: StompSessionWithKxSerialization? = null
    private var connectionAttempts = 0

    suspend fun connectWebSocket(): Result<Unit> = runCatching {
        val accessToken = tokenManager.getAccessToken()

        session = client.connect(
            url = "${BuildConfig.CARE_WEBSOCKET_URL}/ws",
            headers = mapOf("Authorization" to accessToken)
        ).stomp(StompConfig())
            .withJsonConversions(json)

        connectionAttempts = 0
    }.recoverCatching { throwable ->
        if (connectionAttempts < MAX_RETRY_ATTEMPTS) {
            val waitTime = minOf(calculateBackoffTime(connectionAttempts), MAX_WAIT_TIME)
            delay(waitTime)
            connectionAttempts++
            connectWebSocket().getOrThrow()
        } else {
            throw throwable
        }
    }

    suspend fun disconnectWebSocket(reason: String? = null): Result<Unit> = try {
        session?.disconnect()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun subscribeChatMessage(userId: String): Flow<ChatMessageResponse> =
        session?.subscribe(
            StompSubscribeHeaders(destination = "/sub/${userId}"),
            ChatMessageResponse.serializer()
        ) ?: flow { throw IOException("웹소켓을 먼저 연결해주세요.") }

    suspend fun sendMessage(sendChatMessageRequest: SendChatMessageRequest): Result<Unit> =
        runCatching {
            session?.convertAndSend(
                headers = StompSendHeaders(destination = "/pub/send"),
                body = sendChatMessageRequest,
                serializer = SendChatMessageRequest.serializer(),
            )
        }
}
