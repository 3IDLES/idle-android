package com.idle.network.source

import com.idle.domain.model.auth.UserType
import com.idle.network.api.ChatApi
import com.idle.network.di.TokenManager
import com.idle.network.model.chat.ChatResponse
import com.idle.network.model.chat.GenerateChatRoomResponse
import com.idle.network.model.chat.GetChatMessageResponse
import com.idle.network.model.chat.GetChatRoomResponse
import com.idle.network.model.chat.ReadMessageRequest
import com.idle.network.model.chat.SendMessageRequest
import com.idle.network.serializer.ChatResponseSerializer
import com.idle.network.util.MAX_RETRY_ATTEMPTS
import com.idle.network.util.calculateRetryTime
import com.idle.network.util.onResponse
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

class ChatDataSource @Inject constructor(
    private val chatApi: ChatApi,
    private val client: WebSocketClient,
    private val tokenManager: TokenManager,
    private val chatResponseSerializer: ChatResponseSerializer,
    private val json: Json,
) {
    suspend fun getWorkerChatRooms(): List<GetChatRoomResponse> =
        chatApi.getWorkerChatRooms().onResponse()

    suspend fun getCenterChatRooms(): List<GetChatRoomResponse> =
        chatApi.getCenterChatRooms().onResponse()

    suspend fun getWorkerChatRoomMessages(
        roomId: String,
        messageId: String?,
    ): GetChatMessageResponse = chatApi.getWorkerChatRoomMessages(
        chatRoomId = roomId,
        messageId = messageId
    ).onResponse()

    suspend fun getCenterChatRoomMessages(
        roomId: String,
        messageId: String?,
    ): GetChatMessageResponse = chatApi.getCenterChatRoomMessages(
        chatRoomId = roomId,
        messageId = messageId
    ).onResponse()

    suspend fun generateWorkerChatRoom(opponentId: String): GenerateChatRoomResponse =
        chatApi.generateWorkerChatRoom(opponentId).onResponse()

    suspend fun generateCenterChatRoom(opponentId: String): GenerateChatRoomResponse =
        chatApi.generateCenterChatRoom(opponentId).onResponse()

    private var session: StompSessionWithKxSerialization? = null
    private var connectionAttempts = 0

    suspend fun connectWebSocket() {
        val accessToken = tokenManager.getAccessToken()
        try {
            session = client.connect(
                url = "${BuildConfig.CARE_WEBSOCKET_URL}/ws",
                headers = mapOf("Authorization" to accessToken)
            ).stomp(StompConfig())
                .withJsonConversions(json)
            connectionAttempts = 0
        } catch (e: Throwable) {
            if (connectionAttempts < MAX_RETRY_ATTEMPTS) {
                val waitTime = calculateRetryTime(connectionAttempts)
                delay(waitTime)
                connectionAttempts++
                connectWebSocket()
            } else {
                throw e
            }
        }
    }

    suspend fun disconnectWebSocket() {
        session?.disconnect()
    }

    suspend fun subscribeChatMessage(userId: String): Flow<ChatResponse> =
        session?.subscribe(
            StompSubscribeHeaders(destination = "/sub/${'$'}{userId}"),
            chatResponseSerializer,
        ) ?: flow { throw IOException("웹소켓을 먼저 연결해주세요.") }


    suspend fun sendMessage(
        userType: UserType,
        sendMessageRequest: SendMessageRequest
    ) {
        session?.convertAndSend(
            headers = StompSendHeaders(destination = "/pub/send/$${userType.apiValue.lowercase()}"),
            body = sendMessageRequest,
            serializer = SendMessageRequest.serializer(),
        )
    }

    suspend fun readMessage(
        userType: UserType,
        readMessageRequest: ReadMessageRequest
    ) {
        session?.convertAndSend(
            headers = StompSendHeaders(destination = "/pub/read/$${userType.apiValue.lowercase()}"),
            body = readMessageRequest,
            serializer = ReadMessageRequest.serializer(),
        )
    }
}
