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
    fun getNotificationTime(): String {
        val currentTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

        if (createdAt.isAfter(currentTime)) {
            return "곧 시작될 알림"
        }

        val minutesDifference = ChronoUnit.MINUTES.between(createdAt, currentTime)
        val hoursDifference = ChronoUnit.HOURS.between(createdAt, currentTime)
        val daysDifference = ChronoUnit.DAYS.between(createdAt, currentTime)

        return when {
            minutesDifference <= 1 -> "방금 전"
            minutesDifference < 60 -> "${minutesDifference}분 전"
            hoursDifference < 24 -> "${hoursDifference}시간 전"
            daysDifference < 7 -> "${daysDifference}일 전"
            else -> "${daysDifference / 7}주 전"
        }
    }
}

enum class NotificationType(private val notificationTypeClass: Type) {
    APPLICANT(NotificationContent.ApplicantNotification::class.java),
    UNKNOWN(NotificationContent.UnKnownNotification::class.java);

    companion object {
        fun create(notificationType: String?): NotificationType {
            return NotificationType.entries.firstOrNull { it.name == notificationType } ?: UNKNOWN
        }
    }
}

sealed class NotificationContent {
    data class ApplicantNotification(val jobPostingId: String) : NotificationContent()
    data object UnKnownNotification : NotificationContent()
}