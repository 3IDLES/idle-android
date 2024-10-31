package com.idle.network.source.websocket

import com.idle.network.BuildConfig
import com.idle.network.di.WebSocketOkHttpClient
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketDataSource @Inject constructor(
    @WebSocketOkHttpClient private val client: OkHttpClient,
    private val chatMessageListener: ChatMessageListener,
) {
    private lateinit var chatMessageWebSocket: WebSocket

    fun connectWebSocket(): Result<Unit> {
        return try {
            val chatMessageRequest: Request = Request.Builder()
                .url(BuildConfig.CARE_WEBSOCKET_URL)
                .build()

            chatMessageWebSocket = client.newWebSocket(chatMessageRequest, chatMessageListener)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
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

    fun getChatMessageFlow() = chatMessageListener.chatMessageFlow
}