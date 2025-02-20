package com.idle.network.api

import com.idle.network.model.chat.GetChatRoomResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatApi {
    @GET("/api/v2/chat/carer/chatrooms")
    suspend fun getWorkerChatRooms(): Response<List<GetChatRoomResponse>>

    @GET("/api/v2/chat/carer/chatrooms")
    suspend fun generateWorkerChatRoom(@Query("opponentId") opponentId: String): Response<Unit>

    @GET("/api/v2/chat/carer/chatrooms")
    suspend fun generateCenterChatRoom(@Query("opponentId") opponentId: String): Response<Unit>

    @GET("/api/v2/chat/center/chatrooms")
    suspend fun getCenterChatRooms(): Response<List<GetChatRoomResponse>>
}
