package com.idle.domain.model.jobposting

import java.time.LocalDate
import java.time.ZoneId

data class CrawlingJobPosting(
    val id: String,
    val title: String,
    val distance: String,
    val workingTime: String,
    val workingSchedule: String,
    val payInfo: String,
    val applyDeadline: LocalDate,
    val jobPostingType: JobPostingType,
    val createdAt: LocalDate,
) {
    fun calculateDeadline(): Long {
        val seoulZone = ZoneId.of("Asia/Seoul")
        val nowDate = LocalDate.now(seoulZone)
        return applyDeadline.toEpochDay() - nowDate.toEpochDay()
    }
}