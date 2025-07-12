package com.idle.network.source

import com.idle.network.api.NotificationApi
import com.idle.network.model.notification.DeleteFcmTokenRequest
import com.idle.network.model.notification.GetMyNotificationResponse
import com.idle.network.model.notification.GetUnreadNotificationCountResponse
import com.idle.network.model.notification.PostFcmTokenRequest
import com.idle.network.util.onResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationDataSource @Inject constructor(
    private val notificationApi: NotificationApi
) {
    suspend fun postFCMToken(postFcmTokenRequest: PostFcmTokenRequest): Unit =
        notificationApi.postFCMToken(postFcmTokenRequest).onResponse()

    suspend fun deleteFCMToken(deleteFcmTokenRequest: DeleteFcmTokenRequest): Unit =
        notificationApi.deleteFCMToken(deleteFcmTokenRequest).onResponse()

    suspend fun getMyNotifications(
        next: String?,
        limit: Int
    ): GetMyNotificationResponse =
        notificationApi.getMyNotifications(next = next, limit = limit).onResponse()

    suspend fun readNotification(notificationId: String): Unit =
        notificationApi.readNotification(notificationId).onResponse()

    suspend fun getUnreadNotificationCount(): GetUnreadNotificationCountResponse =
        notificationApi.getUnreadNotificationCount().onResponse()
}
