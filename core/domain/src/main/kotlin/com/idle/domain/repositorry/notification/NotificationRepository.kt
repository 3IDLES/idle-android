package com.idle.domain.repositorry.notification

import com.idle.domain.model.notification.Notification

interface NotificationRepository {
    suspend fun getMyNotifications(
        next: String?,
        limit: Int
    ): Result<Pair<String?, List<Notification>>>

    suspend fun readNotification(notificationId: String): Result<Unit>
    suspend fun getUnreadNotificationCount(): Result<Int>
}