package com.idle.domain.model.job

enum class DayOfWeek(val displayName: String) {
    MONDAY("월"),
    TUESDAY("화"),
    WEDNESDAY("수"),
    THURSDAY("목"),
    FRIDAY("금"),
    SATURDAY("토"),
    SUNDAY("일");

    companion object {
        fun create(dayOfWeek: List<String>): Set<DayOfWeek> {
            return DayOfWeek.entries.filter { it.name in dayOfWeek }
                .toSet()
        }
    }
}