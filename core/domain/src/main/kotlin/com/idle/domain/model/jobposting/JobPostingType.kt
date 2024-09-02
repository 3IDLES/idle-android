package com.idle.domain.model.jobposting

enum class JobPostingType {
    CAREMEET, WORKNET, UNKNOWN;

    companion object {
        fun create(value: String?): JobPostingType {
            return JobPostingType.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}