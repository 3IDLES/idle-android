package com.idle.network.source.websocket

import com.idle.network.BuildConfig
import com.idle.network.di.WebSocketOkHttpClient
import com.idle.network.model.chatting.ChatMessageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow

@Singleton
class WebSocketDataSource @Inject constructor(
    @WebSocketOkHttpClient private val client: OkHttpClient,
    private val chatMessageListener: ChatMessageListener,
) {
    val chatMessageFlow: StateFlow<ChatMessageResponse?> = chatMessageListener.chatMessageFlow

    private lateinit var chatMessageWebSocket: WebSocket
    private var connectionAttempts = 0

    suspend fun connectWebSocket(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val chatMessageRequest: Request = Request.Builder()
                .url(BuildConfig.CARE_WEBSOCKET_URL)
                .build()

            chatMessageWebSocket = client.newWebSocket(chatMessageRequest, chatMessageListener)
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

    fun disconnectWebSocket(reason: String? = null): Result<Unit> {
        return try {
            chatMessageWebSocket.let { webSocket ->
                webSocket.close(1000, reason ?: "Normal Closure")
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun calculateBackoffTime(attempt: Int): Long =
        (2.0.pow(attempt) * 1000).toLong()

    companion object {
        private const val MAX_RETRY_ATTEMPTS = 5
        private const val MAX_WAIT_TIME = 10_000L // 10 seconds
    }
}
