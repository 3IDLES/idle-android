package com.idle.domain.repositorry.notification

import com.idle.domain.model.notification.Notification

interface NotificationRepository {
    suspend fun getNotifications(): Result<List<Notification>>
}