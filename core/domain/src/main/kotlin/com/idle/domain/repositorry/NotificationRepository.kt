package com.idle.domain.repositorry

import com.idle.domain.model.notification.Notification

interface NotificationRepository {
    suspend fun getMyNotifications(
        next: String?,
        limit: Int = 10,
    ): Result<Pair<String?, List<Notification>>>

    suspend fun readNotification(notificationId: String): Result<Unit>
    suspend fun getUnreadNotificationCount(): Result<Int>
}
