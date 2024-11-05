package com.idle.network.api

import com.idle.network.model.notification.DeleteFcmTokenRequest
import com.idle.network.model.notification.GetMyNotificationResponse
import com.idle.network.model.notification.GetUnreadNotificationCountResponse
import com.idle.network.model.notification.PostFcmTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {
    @POST("/api/v1/fcm/token")
    suspend fun postFCMToken(@Body postFcmTokenRequest: PostFcmTokenRequest): Response<Unit>

    @HTTP(method = "DELETE", path = "/api/v1/fcm/token", hasBody = true)
    suspend fun deleteFCMToken(@Body deleteFcmTokenRequest: DeleteFcmTokenRequest): Response<Unit>

    @GET("/api/v1/notifications/my")
    suspend fun getMyNotifications(
        @Query("next") next: String?,
        @Query("limit") limit: Int,
    ): Response<GetMyNotificationResponse>

    @PATCH("/api/v1/notifications/{notification-id}")
    suspend fun readNotification(@Path("notification-id") notificationId: String): Response<Unit>

    @GET("/api/v1/notifications/count")
    suspend fun getUnreadNotificationCount(): Response<GetUnreadNotificationCountResponse>
}
