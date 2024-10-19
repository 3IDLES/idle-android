package com.idle.domain.usecase.notification

import com.idle.domain.model.notification.Notification
import com.idle.domain.repositorry.notification.NotificationRepository
import javax.inject.Inject

class GetMyNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(
        next: String?,
        limit: Int = 10
    ): Result<Pair<String?, List<Notification>>> =
        notificationRepository.getMyNotifications(next = next, limit = limit)
}