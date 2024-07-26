package com.idle.domain.model.auth

enum class Gender(val displayName: String) {
    MAN("남성"), WOMAN("여성"), NONE("");

    companion object {
        fun create(value: String?): Gender {
            return Gender.entries.firstOrNull { it.name == value } ?: NONE
        }
    }
}