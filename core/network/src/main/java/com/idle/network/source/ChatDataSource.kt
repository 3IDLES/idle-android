package com.idle.network.source

import com.idle.domain.model.auth.UserType
import com.idle.network.BuildConfig
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
import com.idle.network.util.MAX_WAIT_TIME
import com.idle.network.util.calculateBackoffTime
import com.idle.network.util.safeApiCall
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
    suspend fun getWorkerChatRooms(): Result<List<GetChatRoomResponse>> =
        safeApiCall { chatApi.getWorkerChatRooms() }

    suspend fun getCenterChatRooms(): Result<List<GetChatRoomResponse>> =
        safeApiCall { chatApi.getCenterChatRooms() }

    suspend fun getWorkerChatRoomMessages(
        roomId: String,
        messageId: String?,
    ): Result<GetChatMessageResponse> =
        safeApiCall {
            chatApi.getWorkerChatRoomMessages(
                chatRoomId = roomId,
                messageId = messageId
            )
        }

    suspend fun getCenterChatRoomMessages(
        roomId: String,
        messageId: String?,
    ): Result<GetChatMessageResponse> =
        safeApiCall {
            chatApi.getCenterChatRoomMessages(
                chatRoomId = roomId,
                messageId = messageId
            )
        }

    suspend fun generateWorkerChatRoom(opponentId: String): Result<GenerateChatRoomResponse> =
        safeApiCall { chatApi.generateWorkerChatRoom(opponentId) }

    suspend fun generateCenterChatRoom(opponentId: String): Result<GenerateChatRoomResponse> =
        safeApiCall { chatApi.generateCenterChatRoom(opponentId) }

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

    suspend fun subscribeChatMessage(userId: String): Flow<ChatResponse> =
        session?.subscribe(
            StompSubscribeHeaders(destination = "/sub/${userId}"),
            chatResponseSerializer,
        ) ?: flow { throw IOException("웹소켓을 먼저 연결해주세요.") }

    suspend fun sendMessage(
        userType: UserType,
        sendMessageRequest: SendMessageRequest
    ): Result<Unit> =
        runCatching {
            val result = session?.convertAndSend(
                headers = StompSendHeaders(destination = "/pub/send/${userType.apiValue.lowercase()}"),
                body = sendMessageRequest,
                serializer = SendMessageRequest.serializer(),
            )
        }

    suspend fun readMessage(
        userType: UserType,
        readMessageRequest: ReadMessageRequest
    ): Result<Unit> = runCatching {
        session?.convertAndSend(
            headers = StompSendHeaders(destination = "/pub/read/${userType.apiValue.lowercase()}"),
            body = readMessageRequest,
            serializer = ReadMessageRequest.serializer(),
        )
    }
}
