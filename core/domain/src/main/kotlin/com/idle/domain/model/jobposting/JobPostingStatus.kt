package com.idle.domain.model.jobposting

enum class JobPostingStatus {
    IN_PROGRESS, COMPLETED, UNKNOWN;

    companion object {
        fun create(value: String?): JobPostingStatus {
            return JobPostingStatus.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}