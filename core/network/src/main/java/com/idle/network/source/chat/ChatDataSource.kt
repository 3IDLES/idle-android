package com.idle.network.source.chat

import com.idle.network.api.ChatApi
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
}
