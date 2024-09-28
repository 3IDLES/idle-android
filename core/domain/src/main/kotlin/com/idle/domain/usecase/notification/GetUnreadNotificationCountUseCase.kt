package com.idle.domain.usecase.notification

import com.idle.domain.repositorry.notification.NotificationRepository
import javax.inject.Inject

class GetUnreadNotificationCountUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke() = notificationRepository.getUnreadNotificationCount()
}