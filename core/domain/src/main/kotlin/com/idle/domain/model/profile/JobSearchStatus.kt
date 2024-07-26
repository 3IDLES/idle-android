package com.idle.domain.model.profile

enum class JobSearchStatus {
    YES, NO, UNKNOWN;

    companion object {
        fun create(value: String?): JobSearchStatus {
            return JobSearchStatus.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}