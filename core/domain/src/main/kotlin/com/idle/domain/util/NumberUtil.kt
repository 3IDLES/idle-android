package com.idle.domain.util

fun formatPhoneNumber(phoneNumber: String): String {
    require(
        (phoneNumber.length == 11 && phoneNumber.all { it.isDigit() }) ||
                (phoneNumber.contains("-") && phoneNumber.length == 13)
    ) {
        "전화번호는 11자리 숫자이거나, 이미 포맷된 경우 13자리여야 합니다."
    }

    // 이미 '-'가 포함된 경우 그대로 반환
    return if (phoneNumber.contains("-")) {
        phoneNumber
    } else {
        phoneNumber.replaceFirst(
            Regex("(\\d{3})(\\d{4})(\\d{4})"),
            "$1-$2-$3"
        )
    }
}