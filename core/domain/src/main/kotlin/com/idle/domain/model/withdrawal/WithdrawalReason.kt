package com.idle.domain.model.withdrawal

enum class WithdrawalReason(val displayName: String) {
    MATCHING_NOT_SATISFACTORY("매칭이 잘 이루어지지 않음"),
    INCONVENIENT_PLATFORM_USE("플랫폼 사용에 불편함을 느낌"),
    NO_LONGER_USE_PLATFORM("플랫폼을 더 이상 사용할 이유가 없음"),
    USING_ANOTHER_PLATFORM("다른 플랫폼을 이용하고 있음"),
    LACK_OF_DESIRED_FEATURES("원하는 기능이 부재"),
    PRIVACY_CONCERNS("개인정보 보호 문제"),

    // 센터
    NO_LONGER_OPERATING_CENTER("센터를 더 이상 운영하지 않음"),

    // 요양 보호사
    NO_LONGER_WISH_TO_CONTINUE("더 이상 구직 의사가 없음");
}