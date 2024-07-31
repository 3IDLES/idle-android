package com.idle.domain.model.job

enum class ApplyMethod(val displayName: String) {
    CALLING("전화 지원"),
    MESSAGE("문자 지원"),
    APP("어플 지원");

    companion object {
        fun create(applyMethod: List<String>): List<ApplyMethod> {
            return ApplyMethod.entries.filter { it.name in applyMethod }
        }
    }
}