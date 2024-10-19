package com.idle.data.repository.notification

import com.idle.domain.model.notification.Notification
import com.idle.domain.repositorry.notification.NotificationRepository
import com.idle.network.source.notification.NotificationDataSource
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource,
) : NotificationRepository {
    override suspend fun getMyNotifications(
        next: String?,
        limit: Int
    ): Result<Pair<String?, List<Notification>>> =
        notificationDataSource.getMyNotifications(next = next, limit = limit)
            .map { it.toVO() }

    override suspend fun readNotification(notificationId: String): Result<Unit> =
        notificationDataSource.readNotification(notificationId)

    override suspend fun getUnreadNotificationCount(): Result<Int> =
        notificationDataSource.getUnreadNotificationCount()
            .mapCatching { it.unreadNotificationCount ?: 0 }
}