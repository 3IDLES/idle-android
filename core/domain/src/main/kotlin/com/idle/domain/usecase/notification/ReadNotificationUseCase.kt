package com.idle.domain.usecase.notification

import com.idle.domain.repositorry.notification.NotificationRepository
import javax.inject.Inject

class ReadNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(notificationId: String) =
        notificationRepository.readNotification(notificationId)
}