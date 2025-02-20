package com.idle.network.source.websocket

import android.util.Log
import com.idle.network.BuildConfig
import com.idle.network.model.chat.ChatMessageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatMessageListener @Inject constructor(private val json: Json) : WebSocketListener() {
    private val _chatMessageChannel = MutableStateFlow<ChatMessageResponse?>(null)
    val chatMessageFlow = _chatMessageChannel.asStateFlow()

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)

        if (BuildConfig.DEBUG) {
            Log.d("ChatMessageListener onMessage", text)
        }

        val chatMessageResponse = try {
            json.decodeFromString<ChatMessageResponse>(text)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.d("ChatMessageListener DecodeException", e.toString())
            }
            return
        }

        _chatMessageChannel.value = chatMessageResponse
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
    }
}
