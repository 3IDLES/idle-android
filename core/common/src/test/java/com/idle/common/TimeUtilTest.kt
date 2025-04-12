package com.idle.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class TimeUtilTest {
    @Test
    fun `올바른 형식의 문자열을 DateTime으로 변환할 수 있다`() {
        // given
        val dateTimeString = "2024-06-01T00:00:00"
        val expected = LocalDateTime.parse(dateTimeString)

        // when
        val actual = dateTimeString.parseDateTime()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `null 값을 DateTime으로 파싱하려고 할 경우 LocalDateTime_MIN을 반환한다`() {
        // given
        val nullString: String? = null
        val expected = LocalDateTime.MIN

        // when
        val actual = nullString.parseDateTime()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `형식에 맞지 않은 문자열을 DateTime으로 파싱하려고 할 경우 LocalDateTime_MIN을 반환한다`() {
        // given
        val invalidString = "invalid-date-format"
        val expected = LocalDateTime.MIN

        // when
        val actual = invalidString.parseDateTime()

        // then
        assertEquals(expected, actual)
    }
}
