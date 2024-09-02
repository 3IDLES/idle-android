package com.idle.domain.model.jobposting

enum class MentalStatus(val displayName: String) {
    NORMAL("정상"),
    EARLY_STAGE("치매 초기"),
    OVER_MIDDLE_STAGE("치매 중기 이상"),
    UNKNOWN("");

    companion object {
        fun create(mentalStatus: String): MentalStatus {
            return MentalStatus.entries.firstOrNull { it.name == mentalStatus } ?: UNKNOWN
        }
    }
}