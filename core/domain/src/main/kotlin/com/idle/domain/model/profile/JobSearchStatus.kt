package com.idle.domain.model.profile

enum class JobSearchStatus(val displayName: String) {
    YES("구인중"), NO("근무중"), UNKNOWN("로드중");

    companion object {
        fun create(value: String?): JobSearchStatus {
            return JobSearchStatus.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}