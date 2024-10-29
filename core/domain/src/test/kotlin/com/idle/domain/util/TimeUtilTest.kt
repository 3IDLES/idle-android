package com.idle.domain.util

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TimeUtilTest {

    private val seoulZone = ZoneId.of("Asia/Seoul")

    @Test
    fun `현재보다 이후의 시간은 '미래'로 표기된다`() {
        // Given
        val futureDateTime = LocalDateTime.now(seoulZone).plusDays(1)

        // When
        val result = futureDateTime.formatRelativeDateTime()

        // Then
        assertEquals("미래", result)
    }

    @Test
    fun `하루 이내의 과거 시간은 '오후 XX시 XX분' 형식으로 표기된다`() {
        // Given
        val pastDateTime = LocalDateTime.now(seoulZone).minusHours(3)

        // When
        val result = pastDateTime.formatRelativeDateTime()

        // Then
        // 오후/오전 표기는 테스트 시스템의 현지 시간에 따라 변경될 수 있음
        val expectedTime = pastDateTime.format(DateTimeFormatter.ofPattern("a hh시 mm분"))
            .replace("AM", "오전")
            .replace("PM", "오후")
        assertEquals(expectedTime, result)
    }

    @Test
    fun `하루 이상 지난 경우 X월 X일 형식으로 표기된다`() {
        // Given
        val pastDateTime = LocalDateTime.now(seoulZone).minusDays(5)

        // When
        val result = pastDateTime.formatRelativeDateTime()

        // Then
        val expectedTime = pastDateTime.format(DateTimeFormatter.ofPattern("M월 d일"))
        assertEquals(expectedTime, result)
    }

    @Test
    fun `1분 전에 도착한 시간은 방금 전이라고 표기된다`() {
        // Given
        val time = LocalDateTime.now(seoulZone)

        // When
        val result = time.formatRelativeTimeDescription()

        // Then
        assertEquals("방금 전", result)
    }

    @Test
    fun `1분 이후의 시간은 분 단위로 표기된다`() {
        // Given
        val time = LocalDateTime.now(seoulZone).minusMinutes(3)

        // When
        val result = time.formatRelativeTimeDescription()

        // Then
        assertEquals("3분 전", result)
    }

    @Test
    fun `60분 이후의 시간은 시간 단위로 표기된다`() {
        // Given
        val time = LocalDateTime.now(seoulZone).minusHours(2).minusMinutes(15)

        // When
        val result = time.formatRelativeTimeDescription()

        // Then
        assertEquals("2시간 전", result)
    }

    @Test
    fun `24시간 이후의 시간은 일 단위로 표기된다`() {
        // Given
        val time = LocalDateTime.now(seoulZone).minusHours(25)

        // When
        val result = time.formatRelativeTimeDescription()

        // Then
        assertEquals("1일 전", result)
    }

    @Test
    fun `7일 이후의 시간은 주 단위로 표기된다`() {
        // Given
        val time = LocalDateTime.now(seoulZone).minusDays(8)

        // When
        val result = time.formatRelativeTimeDescription()

        // Then
        assertEquals("1주 전", result)
    }

    @Test
    fun `현재 시간 보다 이후의 시간은 미래라고 표기된다`() {
        // Given
        val time = LocalDateTime.now(seoulZone).plusHours(2)

        // When
        val result = time.formatRelativeTimeDescription()

        // Then
        assertEquals("미래", result)
    }
}