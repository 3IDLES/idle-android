package com.idle.domain.model.jobposting

enum class LifeAssistance(val displayName: String) {
    CLEANING("청소"),
    LAUNDRY("빨래"),
    WALKING("산책"),
    HEALTH("운동보조"),
    TALKING("말벗"),
    NONE("");

    companion object {
        fun create(lifeAssistance: List<String>): Set<LifeAssistance> {
            return LifeAssistance.entries.filter { it.name in lifeAssistance }
                .toSet()
        }
    }
}