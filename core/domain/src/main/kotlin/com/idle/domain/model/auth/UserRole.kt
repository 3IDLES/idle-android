package com.idle.domain.model.auth

import com.idle.domain.model.auth.Gender.NONE

enum class UserRole(val apiValue: String) {
    CENTER("center"), WORKER("carer");

    companion object {
        fun create(value: String?): UserRole {
            return UserRole.entries.firstOrNull { it.name == value } ?: CENTER
        }
    }
}