package com.idle.domain.usecase.notification

import com.idle.domain.model.notification.Notification
import com.idle.domain.repositorry.notification.NotificationRepository
import javax.inject.Inject

class GetMyNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): Result<List<Notification>> = notificationRepository.getMyNotifications()
}