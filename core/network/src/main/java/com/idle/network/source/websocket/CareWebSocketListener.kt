package com.idle.network.source.websocket

import android.util.Log
import com.idle.network.BuildConfig
import com.idle.network.model.chatting.ChatMessageResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.Json
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatMessageListener @Inject constructor(private val json: Json) : WebSocketListener() {
    private val _chatMessageChannel = Channel<ChatMessageResponse>(Channel.BUFFERED)
    val chatMessageFlow = _chatMessageChannel.receiveAsFlow()

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

        _chatMessageChannel.trySend(chatMessageResponse)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
    }
}