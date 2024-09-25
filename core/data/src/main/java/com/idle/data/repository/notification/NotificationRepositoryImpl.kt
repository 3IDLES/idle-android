package com.idle.data.repository.notification

import com.idle.domain.model.notification.Notification
import com.idle.domain.repositorry.notification.NotificationRepository
import com.idle.network.source.notification.NotificationDataSource
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource,
) : NotificationRepository {
    override suspend fun getNotifications(): Result<List<Notification>> =
        notificationDataSource.getNotifications()
            .mapCatching {
                it.notifications.map { response ->
                    Notification(response)
                }
            }
}