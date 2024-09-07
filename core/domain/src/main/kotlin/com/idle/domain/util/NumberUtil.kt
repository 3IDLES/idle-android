package com.idle.domain.util

internal fun formatPhoneNumber(phoneNumber: String): String {
    // 입력된 전화번호가 11자리 숫자인 경우에만 포맷팅을 적용
    return if (phoneNumber.length == 11) {
        phoneNumber.replaceFirst(
            Regex("(\\d{3})(\\d{4})(\\d{4})"),
            "$1-$2-$3"
        )
    } else {
        phoneNumber  // 비정상적인 경우 원본 번호 반환
    }
}