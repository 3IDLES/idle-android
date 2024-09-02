package com.idle.network.model.jobposting

import com.idle.domain.model.jobposting.CrawlingJobPosting
import com.idle.domain.model.jobposting.JobPostingType
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class GetCrawlingJobPostingsResponse(
    val items: List<CrawlingJobPostingResponse> = listOf(),
    val next: String? = null,
    val total: Int = 0,
)

@Serializable
data class CrawlingJobPostingResponse(
    val id: String,
    val title: String,
    val distance: String,
    val workingTime: String,
    val workingSchedule: String,
    val payInfo: String,
    val applyDeadline: String,
    val jobPostingType: String,
    val createdAt: String,
) {
    fun toVO() = CrawlingJobPosting(
        id = id,
        title = title,
        distance = distance,
        workingTime = workingTime,
        workingSchedule = workingSchedule,
        payInfo = payInfo,
        applyDeadline = LocalDate.parse(applyDeadline, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        jobPostingType = JobPostingType.create(jobPostingType),
        createdAt = LocalDate.parse(createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
    )
}
