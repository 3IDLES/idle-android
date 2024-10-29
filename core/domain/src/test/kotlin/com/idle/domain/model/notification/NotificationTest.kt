package com.idle.domain.model.notification

import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId
import org.junit.jupiter.api.Assertions.assertEquals

class NotificationTest {

    @Test
    fun `1분 전에 도착한 알림은 방금 전이라고 표기된다`() {
        // Given
        val notification = Notification(
            notificationType = NotificationType.APPLICANT,
            id = "1",
            isRead = false,
            title = "지원 알림",
            body = "지원이 완료되었습니다.",
            imageUrl = null,
            createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul")),
            notificationDetails = NotificationContent.ApplicantNotification("jobId")
        )

        // When
        val result = notification.getNotificationTime()

        // Then
        assertEquals("방금 전", result)
    }

    @Test
    fun `1분 이후의 알림은 분 단위로 표기된다`() {
        // Given
        val notification = Notification(
            notificationType = NotificationType.APPLICANT,
            id = "2",
            isRead = false,
            title = "지원 알림",
            body = "지원이 완료되었습니다.",
            imageUrl = null,
            createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusMinutes(3),
            notificationDetails = NotificationContent.ApplicantNotification("jobId")
        )

        // When
        val result = notification.getNotificationTime()

        // Then
        assertEquals("3분 전", result)
    }

    @Test
    fun `60분 이후의 알림은 시간 단위로 표기된다`() {
        // Given
        val notification = Notification(
            notificationType = NotificationType.APPLICANT,
            id = "3",
            isRead = false,
            title = "지원 알림",
            body = "지원이 완료되었습니다.",
            imageUrl = null,
            createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusHours(2).minusMinutes(15),
            notificationDetails = NotificationContent.ApplicantNotification("jobId")
        )

        // When
        val result = notification.getNotificationTime()

        // Then
        assertEquals("2시간 전", result)
    }

    @Test
    fun `24시간 이후의 알림은 일 단위로 표기된다`() {
        // Given
        val notification = Notification(
            notificationType = NotificationType.APPLICANT,
            id = "4",
            isRead = false,
            title = "지원 알림",
            body = "지원이 완료되었습니다.",
            imageUrl = null,
            createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusHours(25),
            notificationDetails = NotificationContent.ApplicantNotification("jobId")
        )

        // When
        val result = notification.getNotificationTime()

        // Then
        assertEquals("1일 전", result)
    }

    @Test
    fun `7일 이후의 알림은 알림은 주 단위로 표기된다`() {
        // Given
        val notification = Notification(
            notificationType = NotificationType.APPLICANT,
            id = "5",
            isRead = false,
            title = "지원 알림",
            body = "지원이 완료되었습니다.",
            imageUrl = null,
            createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(8),
            notificationDetails = NotificationContent.ApplicantNotification("jobId")
        )

        // When
        val result = notification.getNotificationTime()

        // Then
        assertEquals("1주 전", result)
    }

    @Test
    fun `현재 시간 보다 알림 생성시간이 늦을 경우 곧 시작될 알림이라고 표기한다`() {
        // Given
        val notification = Notification(
            notificationType = NotificationType.APPLICANT,
            id = "6",
            isRead = false,
            title = "지원 알림",
            body = "지원이 완료되었습니다.",
            imageUrl = null,
            createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(2),
            notificationDetails = NotificationContent.ApplicantNotification("jobId")
        )

        // When
        val result = notification.getNotificationTime()

        // Then
        assertEquals("곧 시작될 알림", result)
    }
}
