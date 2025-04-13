package com.idle.network.api

import com.idle.network.model.chat.ChatMessageResponse
import com.idle.network.model.chat.GenerateChatRoomResponse
import com.idle.network.model.chat.GetChatRoomResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {
    @GET("/api/v2/chat/carer/chatrooms")
    suspend fun getWorkerChatRooms(): Response<List<GetChatRoomResponse>>

    @GET("/api/v2/chat/center/chatrooms")
    suspend fun getCenterChatRooms(): Response<List<GetChatRoomResponse>>

    @POST("/api/v2/chat/carer/chatrooms")
    suspend fun generateWorkerChatRoom(@Query("opponentId") opponentId: String): Response<GenerateChatRoomResponse>

    @POST("/api/v2/chat/carer/chatrooms")
    suspend fun generateCenterChatRoom(@Query("opponentId") opponentId: String): Response<GenerateChatRoomResponse>

    @GET("/api/v2/chat/carer/chatrooms/{chatroom-id}/messages")
    suspend fun getWorkerChatRoomMessages(
        @Path("chatroom-id") chatRoomId: String,
        @Query("message-id") messageId: String?,
    ): Response<List<ChatMessageResponse>>

    @GET("/api/v2/chat/center/chatrooms/{chatroom-id}/messages")
    suspend fun getCenterChatRoomMessages(
        @Path("chatroom-id") chatRoomId: String,
        @Query("message-id") messageId: String?,
    ): Response<List<ChatMessageResponse>>
}
