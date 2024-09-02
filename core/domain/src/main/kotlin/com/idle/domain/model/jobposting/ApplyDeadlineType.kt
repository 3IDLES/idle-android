package com.idle.domain.model.jobposting

enum class ApplyDeadlineType(val displayName: String) {
    UNLIMITED("채용시까지"),
    LIMITED("마감일 설정"),
    UNKNOWN("");

    companion object {
        fun create(applyDeadlineType: String): ApplyDeadlineType {
            return ApplyDeadlineType.entries.firstOrNull { it.name in applyDeadlineType } ?: UNKNOWN
        }
    }
}