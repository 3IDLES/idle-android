package com.idle.data.repository

import com.idle.domain.model.notification.Notification
import com.idle.domain.repositorry.NotificationRepository
import com.idle.network.source.NotificationDataSource
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource,
) : NotificationRepository {
    override suspend fun getMyNotifications(
        next: String?,
        limit: Int
    ): Pair<String?, List<Notification>> =
        notificationDataSource.getMyNotifications(next = next, limit = limit).toVO()

    override suspend fun readNotification(notificationId: String) =
        notificationDataSource.readNotification(notificationId)

    override suspend fun getUnreadNotificationCount(): Int =
        notificationDataSource.getUnreadNotificationCount()
            .unreadNotificationCount ?: 0
}
