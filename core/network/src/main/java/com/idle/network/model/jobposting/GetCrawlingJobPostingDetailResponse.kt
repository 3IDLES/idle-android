package com.idle.network.model.jobposting

import com.idle.domain.model.jobposting.CrawlingJobPostingDetail
import com.idle.domain.model.jobposting.JobPostingType
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class GetCrawlingJobPostingDetailResponse(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val clientAddress: String = "",
    val longitude: String = "",
    val latitude: String = "",
    val createdAt: String = "",
    val payInfo: String = "",
    val workingTime: String = "",
    val workingSchedule: String = "",
    val applyDeadline: String = "",
    val recruitmentProcess: String = "",
    val applyMethod: String = "",
    val requireDocumentation: String = "",
    val centerName: String = "",
    val centerAddress: String = "",
    val jobPostingUrl: String = "",
    val jobPostingType: String = "",
    val isFavorite: Boolean = false,
) {
    fun toVO() = CrawlingJobPostingDetail(
        id = id,
        title = title,
        content = content,
        clientAddress = clientAddress,
        longitude = longitude,
        latitude = latitude,
        createdAt = LocalDate.parse(createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        payInfo = payInfo,
        workingTime = workingTime,
        workingSchedule = workingSchedule,
        applyDeadline = LocalDate.parse(applyDeadline, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        recruitmentProcess = recruitmentProcess,
        applyMethod = applyMethod,
        requireDocumentation = requireDocumentation,
        centerName = centerName,
        centerAddress = centerAddress,
        jobPostingUrl = jobPostingUrl,
        jobPostingType = JobPostingType.create(jobPostingType),
        isFavorite = isFavorite,
    )
}
