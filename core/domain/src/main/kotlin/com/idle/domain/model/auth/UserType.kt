package com.idle.domain.model.auth

enum class UserType(val apiValue: String) {
    CENTER("center"), WORKER("carer");

    companion object {
        fun create(value: String?): UserType {
            return UserType.entries.firstOrNull { it.name == value } ?: CENTER
        }
    }
}