package com.idle.network.source.notification

import com.idle.network.api.NotificationApi
import com.idle.network.model.auth.FCMTokenRequest
import com.idle.network.model.notification.GetNotificationResponse
import com.idle.network.model.notification.GetUnreadNotificationCountResponse
import com.idle.network.util.safeApiCall
import javax.inject.Inject

class NotificationDataSource @Inject constructor(
    private val notificationApi: NotificationApi
) {
    suspend fun postFCMToken(fcmTokenRequest: FCMTokenRequest): Result<Unit> =
        safeApiCall { notificationApi.postFCMToken(fcmTokenRequest) }

    suspend fun deleteFCMToken(): Result<Unit> = safeApiCall { notificationApi.deleteFCMToken() }

    suspend fun getMyNotifications(): Result<GetNotificationResponse> =
        safeApiCall { notificationApi.getMyNotifications() }

    suspend fun readNotification(notificationId: String): Result<Unit> =
        safeApiCall { notificationApi.readNotification(notificationId) }

    suspend fun getUnreadNotificationCount(): Result<GetUnreadNotificationCountResponse> =
        safeApiCall { notificationApi.getUnreadNotificationCount() }
}