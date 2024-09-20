package com.idle.domain.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class NumberUtilKtTest {
    @Test
    fun `전화번호가 11자리 숫자일 경우 포맷이 적용된다`() {
        // Given: 11자리 숫자 전화번호
        val phoneNumber = "01012345678"

        // When: 포맷 함수를 호출
        val formatted = formatPhoneNumber(phoneNumber)

        // Then: 올바른 포맷이 적용된 전화번호를 반환
        assertEquals("010-1234-5678", formatted)
    }

    @Test
    fun `이미 '-'가 포함된 13자리 전화번호는 그대로 반환된다`() {
        // Given: 이미 '-'가 포함된 올바른 13자리 전화번호
        val phoneNumber = "010-1234-5678"

        // When: 포맷 함수를 호출
        val formatted = formatPhoneNumber(phoneNumber)

        // Then: 동일한 전화번호를 반환
        assertEquals(phoneNumber, formatted)
    }

    @Test
    fun `잘못된 형식의 13자리 전화번호는 예외가 발생한다`() {
        // Given: 잘못된 형식의 13자리 전화번호
        val phoneNumber = "010-123-56789"

        // When & Then: 예외가 발생하는지 검증
        assertThrows<IllegalArgumentException> {
            formatPhoneNumber(phoneNumber)
        }
    }

    @Test
    fun `전화번호가 11자리가 아니고 '-'가 없는 경우 예외가 발생한다`() {
        // Given: 11자리가 아닌 전화번호
        val phoneNumber = "0101234567"

        // When & Then: 예외가 발생하는지 검증
        assertThrows<IllegalArgumentException> {
            formatPhoneNumber(phoneNumber)
        }
    }

    @Test
    fun `전화번호에 숫자가 아닌 문자가 포함된 경우 예외가 발생한다`() {
        // Given: 전화번호에 문자가 포함된 경우
        val phoneNumber = "01012a45678"

        // When & Then: 예외가 발생하는지 검증
        assertThrows<IllegalArgumentException> {
            formatPhoneNumber(phoneNumber)
        }
    }

    @Test
    fun `사업자 등록번호가 10자리 숫자일 경우 포맷이 적용된다`() {
        // Given: 10자리 숫자 사업자 등록번호
        val businessRegistrationNumber = "1234567890"

        // When: 포맷 함수를 호출
        val formatted = formatBusinessRegistrationNumber(businessRegistrationNumber)

        // Then: 올바른 포맷이 적용된 사업자 등록번호를 반환
        assertEquals("123-45-67890", formatted)
    }

    @Test
    fun `이미 '-'가 포함된 12자리 사업자 등록번호는 그대로 반환된다`() {
        // Given: 이미 '-'가 포함된 12자리 사업자 등록번호
        val businessRegistrationNumber = "123-45-67890"

        // When: 포맷 함수를 호출
        val formatted = formatBusinessRegistrationNumber(businessRegistrationNumber)

        // Then: 동일한 사업자 등록번호를 반환
        assertEquals(businessRegistrationNumber, formatted)
    }

    @Test
    fun `사업자 등록번호가 10자리도 12자리도 아닌 경우 예외가 발생한다`() {
        // Given: 11자리 사업자 등록번호 (비정상적인 경우)
        val businessRegistrationNumber = "12345678901"

        // When & Then: 예외가 발생하는지 검증
        assertThrows<IllegalArgumentException> {
            formatBusinessRegistrationNumber(businessRegistrationNumber)
        }
    }

    @Test
    fun `사업자 등록번호에 숫자가 아닌 문자가 포함된 경우 예외가 발생한다`() {
        // Given: 사업자 등록번호에 문자가 포함된 경우
        val businessRegistrationNumber = "123a567890"

        // When & Then: 예외가 발생하는지 검증
        assertThrows<IllegalArgumentException> {
            formatBusinessRegistrationNumber(businessRegistrationNumber)
        }
    }

    @Test
    fun `잘못된 형식의 12자리 사업자 등록번호는 예외가 발생한다`() {
        // Given: 잘못된 형식의 사업자 등록번호
        val businessRegistrationNumber = "123-456-7890"

        // When & Then: 예외가 발생하는지 검증
        assertThrows<IllegalArgumentException> {
            formatBusinessRegistrationNumber(businessRegistrationNumber)
        }
    }
}