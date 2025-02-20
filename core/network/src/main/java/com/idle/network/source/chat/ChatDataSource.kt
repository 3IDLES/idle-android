package com.idle.network.source.chat

import com.idle.network.api.ChatApi
import com.idle.network.model.chat.ChatMessageResponse
import com.idle.network.model.chat.GenerateChatRoomResponse
import com.idle.network.model.chat.GetChatRoomResponse
import com.idle.network.util.safeApiCall
import javax.inject.Inject

class ChatDataSource @Inject constructor(
    private val chatApi: ChatApi,
) {
    suspend fun getWorkerChatRooms(): Result<List<GetChatRoomResponse>> =
        safeApiCall { chatApi.getWorkerChatRooms() }

    suspend fun getCenterChatRooms(): Result<List<GetChatRoomResponse>> =
        safeApiCall { chatApi.getCenterChatRooms() }

    suspend fun getWorkerChatRoomMessages(
        roomId: String,
        messageId: String?,
    ): Result<List<ChatMessageResponse>> =
        safeApiCall {
            chatApi.getWorkerChatRoomMessages(
                chatRoomId = roomId,
                messageId = messageId
            )
        }

    suspend fun getCenterChatRoomMessages(
        roomId: String,
        messageId: String?,
    ): Result<List<ChatMessageResponse>> =
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
}
