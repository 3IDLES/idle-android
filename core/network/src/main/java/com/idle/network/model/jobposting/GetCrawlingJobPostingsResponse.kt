package com.idle.network.model.jobposting

import com.idle.domain.model.jobposting.CrawlingJobPosting
import com.idle.domain.model.jobposting.CrawlingJobPostingPage
import com.idle.domain.model.jobposting.JobPostingType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCrawlingJobPostingsResponse(
    @SerialName("items") val crawlingJobPostingResponses: List<CrawlingJobPostingResponse> = listOf(),
    val next: String? = null,
    val total: Int = 0,
    val nextDistance: Int = 15,
) {
    fun toVO(): CrawlingJobPostingPage = CrawlingJobPostingPage(
        nextCursor = next,
        items = crawlingJobPostingResponses.map { it.toVO() },
        nextDistance = nextDistance,
    )
}

@Serializable
data class CrawlingJobPostingResponse(
    val id: String,
    val title: String,
    val distance: Int,
    val workingTime: String,
    val workingSchedule: String,
    val payInfo: String,
    val applyDeadline: String,
    val jobPostingType: String,
    val isFavorite: Boolean,
) {
    fun toVO() = CrawlingJobPosting(
        id = id,
        title = title,
        distance = distance,
        workingTime = workingTime,
        workingSchedule = workingSchedule,
        payInfo = payInfo,
        applyDeadline = applyDeadline,
        jobPostingType = JobPostingType.create(jobPostingType),
        isFavorite = isFavorite,
    )
}
