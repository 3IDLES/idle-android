package com.idle.network.source.notification

import com.idle.network.api.NotificationApi
import com.idle.network.model.notification.DeleteFcmTokenRequest
import com.idle.network.model.notification.PostFcmTokenRequest
import com.idle.network.model.notification.GetMyNotificationResponse
import com.idle.network.model.notification.GetUnreadNotificationCountResponse
import com.idle.network.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationDataSource @Inject constructor(
    private val notificationApi: NotificationApi
) {
    suspend fun postFCMToken(postFcmTokenRequest: PostFcmTokenRequest): Result<Unit> =
        safeApiCall { notificationApi.postFCMToken(postFcmTokenRequest) }

    suspend fun deleteFCMToken(deleteFcmTokenRequest: DeleteFcmTokenRequest): Result<Unit> =
        safeApiCall { notificationApi.deleteFCMToken(deleteFcmTokenRequest) }

    suspend fun getMyNotifications(
        next: String?,
        limit: Int
    ): Result<GetMyNotificationResponse> =
        safeApiCall { notificationApi.getMyNotifications(next = next, limit = limit) }

    suspend fun readNotification(notificationId: String): Result<Unit> =
        safeApiCall { notificationApi.readNotification(notificationId) }

    suspend fun getUnreadNotificationCount(): Result<GetUnreadNotificationCountResponse> =
        safeApiCall { notificationApi.getUnreadNotificationCount() }
}