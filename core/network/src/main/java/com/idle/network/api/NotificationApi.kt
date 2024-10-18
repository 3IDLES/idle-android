package com.idle.network.api

import com.idle.domain.model.notification.Notification
import com.idle.network.model.auth.FCMTokenRequest
import com.idle.network.model.notification.GetUnreadNotificationCountResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {
    @POST("/api/v1/fcm/token")
    suspend fun postFCMToken(@Body fcmTokenRequest: FCMTokenRequest): Response<Unit>

    @PATCH("/api/v1/fcm/token")
    suspend fun patchFCMToken(@Body fcmTokenRequest: FCMTokenRequest): Response<Unit>

    @DELETE("/api/v1/fcm/token")
    suspend fun deleteFCMToken(): Response<Unit>

    @GET("/api/v1/notifications/my")
    suspend fun getMyNotifications(
        @Query("next") next: String?,
        @Query("limit") limit: Int,
    ): Response<Pair<String?, List<Notification>>>

    @PATCH("/api/v1/notifications/{notification-id}")
    suspend fun readNotification(@Path("notification-id") notificationId: String): Response<Unit>

    @GET("/api/v1/notifications/count")
    suspend fun getUnreadNotificationCount(): Response<GetUnreadNotificationCountResponse>
}