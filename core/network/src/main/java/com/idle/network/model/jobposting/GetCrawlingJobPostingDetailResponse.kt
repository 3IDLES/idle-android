package com.idle.network.model.jobposting

import com.idle.domain.model.jobposting.CrawlingJobPostingDetail
import com.idle.domain.model.jobposting.JobPostingType
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class GetCrawlingJobPostingDetailResponse(
    val id: String? = null,
    val title: String? = null,
    val content: String? = null,
    val clientAddress: String? = null,
    val longitude: String? = null,
    val latitude: String? = null,
    val createdAt: String? = null,
    val payInfo: String? = null,
    val workingTime: String? = null,
    val workingSchedule: String? = null,
    val applyDeadline: String? = null,
    val recruitmentProcess: String? = null,
    val applyMethod: String? = null,
    val requireDocumentation: String? = null,
    val centerName: String? = null,
    val centerAddress: String? = null,
    val jobPostingUrl: String? = null,
    val jobPostingType: String? = null,
    val distance: Int? = null,
    val isFavorite: Boolean? = null,
) {
    fun toVO() = CrawlingJobPostingDetail(
        id = id ?: "",
        title = title ?: "",
        content = content ?: "",
        clientAddress = clientAddress ?: "",
        longitude = longitude ?: "0.0",
        latitude = latitude ?: "0.0",
        createdAt = createdAt?.let {
            LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } ?: LocalDate.now(),
        payInfo = payInfo ?: "",
        workingTime = workingTime ?: "",
        workingSchedule = workingSchedule ?: "",
        applyDeadline = applyDeadline ?: "",
        recruitmentProcess = recruitmentProcess ?: "",
        applyMethod = applyMethod ?: "",
        requireDocumentation = requireDocumentation ?: "",
        centerName = centerName ?: "",
        centerAddress = centerAddress ?: "",
        jobPostingUrl = jobPostingUrl ?: "",
        jobPostingType = JobPostingType.create(jobPostingType ?: ""),
        isFavorite = isFavorite ?: false,
        distance = distance ?: 0,
    )
}