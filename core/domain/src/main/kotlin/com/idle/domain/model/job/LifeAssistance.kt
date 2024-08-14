package com.idle.domain.model.job

import com.idle.domain.model.job.MentalStatus.UNKNOWN

enum class LifeAssistance(val displayName: String) {
    CLEANING("청소"),
    LAUNDRY("빨래"),
    WALKING("산책"),
    HEALTH("운동보조"),
    TALKING("말벗"),
    NONE("");

    companion object {
        fun create(lifeAssistance: List<String>): List<LifeAssistance> {
            return LifeAssistance.entries.filter { it.name in lifeAssistance }
        }
    }
}