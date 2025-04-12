package com.idle.network.api

import com.idle.network.model.chat.GenerateChatRoomResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatApi {
    @POST("/api/v2/chat/carer/chatrooms")
    suspend fun generateWorkerChatRoom(@Query("opponentId") opponentId: String): Response<GenerateChatRoomResponse>

    @POST("/api/v2/chat/carer/chatrooms")
    suspend fun generateCenterChatRoom(@Query("opponentId") opponentId: String): Response<GenerateChatRoomResponse>
}
