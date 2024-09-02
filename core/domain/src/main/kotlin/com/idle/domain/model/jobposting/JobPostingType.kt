package com.idle.domain.model.jobposting

enum class JobPostingType {
    IN_APP, WORKNET, UNKNOWN;

    companion object {
        fun create(value: String?): JobPostingType {
            return JobPostingType.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}