package com.idle.common

import java.time.LocalDateTime
import java.time.format.DateTimeParseException

/**
 * String?을 LocalDateTime으로 변환합니다.
 *
 * - 문자열이 null인 경우, [LocalDateTime.MIN]을 반환합니다.
 * - 잘못된 형식으로 인해 파싱에 실패할 경우, [LocalDateTime.MIN]을 반환합니다.
 */
fun String?.parseDateTime(): LocalDateTime {
    return try {
        this?.let { LocalDateTime.parse(it) } ?: LocalDateTime.MIN
    } catch (e: DateTimeParseException) {
        LocalDateTime.MIN
    }
}
