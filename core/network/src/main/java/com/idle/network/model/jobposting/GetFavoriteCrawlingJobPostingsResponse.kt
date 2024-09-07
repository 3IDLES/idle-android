package com.idle.network.model.jobposting

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.ApplyDeadlineType
import com.idle.domain.model.jobposting.CrawlingJobPosting
import com.idle.domain.model.jobposting.DayOfWeek
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.jobposting.PayType
import com.idle.domain.model.jobposting.WorkerJobPosting
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class GetFavoriteCrawlingJobPostingsResponse(
    @SerialName("favoriteJobPostings") val workerJobPostingResponses: List<CrawlingJobPostingResponse>,
) {
    fun toVO(): List<CrawlingJobPosting> =
        workerJobPostingResponses.map { it.toVO() }
}