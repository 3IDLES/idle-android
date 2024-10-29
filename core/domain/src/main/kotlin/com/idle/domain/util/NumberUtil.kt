package com.idle.domain.util

fun formatPhoneNumber(phoneNumber: String): String {
    require(
        (phoneNumber.length == 11 && phoneNumber.all { it.isDigit() }) ||
                (phoneNumber.length == 13 && Regex("\\d{3}-\\d{4}-\\d{4}").matches(phoneNumber))
    ) {
        "전화번호는 11자리 숫자이거나, 이미 포맷된 경우 {3}-{4}-{4} 형식의 13자리여야 합니다."
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

fun formatBusinessRegistrationNumber(businessRegistrationNumber: String): String {
    require(businessRegistrationNumber.all { it.isDigit() || it == '-' }) {
        "사업자 등록번호는 숫자 또는 '-'만 포함해야 합니다."
    }

    return when (businessRegistrationNumber.length) {
        10 -> businessRegistrationNumber.replaceFirst(
            Regex("(\\d{3})(\\d{2})(\\d{5})"),
            "$1-$2-$3"
        )

        12 -> {
            require(Regex("\\d{3}-\\d{2}-\\d{5}").matches(businessRegistrationNumber)) {
                "사업자 등록번호 형식이 맞지 않습니다. {3}-{2}-{5} 형식이어야 합니다."
            }
            businessRegistrationNumber
        }

        else -> throw IllegalArgumentException("사업자 등록번호 형식이 맞지 않습니다.")
    }
}