package com.idle.domain.model.jobposting

enum class PayType(val displayName: String) {
    HOURLY("시급"),
    WEEKLY("주급"),
    MONTHLY("월급"),
    UNKNOWN("");

    companion object {
        fun create(payType: String): PayType {
            return PayType.entries.firstOrNull { it.name == payType } ?: UNKNOWN
        }
    }
}