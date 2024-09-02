package com.idle.domain.model.jobposting

import java.time.LocalDate
import java.time.ZoneId

data class CrawlingJobPostingDetail(
    val id: String,
    val title: String,
    val content: String,
    val clientAddress: String,
    val longitude: String,
    val latitude: String,
    val createdAt: LocalDate,
    val payInfo: String,
    val workingTime: String,
    val workingSchedule: String,
    val applyDeadline: LocalDate,
    val recruitmentProcess: String,
    val applyMethod: String,
    val requireDocumentation: String,
    val centerName: String,
    val centerAddress: String,
    val jobPostingUrl: String,
    val jobPostingType: JobPostingType,
    val isFavorite: Boolean = false,
) {
    fun calculateDeadline(): Long {
        val seoulZone = ZoneId.of("Asia/Seoul")
        val nowDate = LocalDate.now(seoulZone)
        return applyDeadline.toEpochDay() - nowDate.toEpochDay()
    }
}