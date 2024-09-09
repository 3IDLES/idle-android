package com.idle.domain.model.jobposting

enum class ApplyMethod(val displayName: String) {
    CALLING("전화 지원"),
    APP("어플 지원");

    companion object {
        fun create(applyMethod: List<String>): Set<ApplyMethod> {
            return ApplyMethod.entries.filter { it.name in applyMethod }
                .toSet()
        }
    }
}