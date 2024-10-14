package com.idle.domain.model.notification

import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

data class Notification(
    val notificationType: NotificationType,
    val id: String,
    val isRead: Boolean,
    val title: String,
    val body: String,
    val imageUrl: String?,
    val createdAt: LocalDateTime,
    val notificationDetails: NotificationContent,
) {
    fun daysSinceCreation(): Long {
        val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        return ChronoUnit.DAYS.between(createdAt, now)
    }
}

enum class NotificationType(private val notificationTypeClass: Type) {
    APPLICANT(NotificationContent.ApplicantNotification::class.java),
    UNKNOWN(NotificationContent.UnKnownNotification::class.java);

    companion object {
        fun create(notificationType: String?): NotificationType {
            return NotificationType.entries.firstOrNull { it.name == notificationType } ?: UNKNOWN
        }

        fun findNotificationClassByType(notificationType: NotificationType): Type {
            return notificationType.notificationTypeClass
        }
    }
}

sealed class NotificationContent {
    data class ApplicantNotification(val jobPostingId: String) : NotificationContent()
    data object UnKnownNotification : NotificationContent()
}