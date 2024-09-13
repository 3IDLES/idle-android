package com.idle.network.model.jobposting

import com.idle.domain.model.jobposting.CrawlingJobPosting
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetFavoriteCrawlingJobPostingsResponse(
    @SerialName("favoriteCrawlingJobPostings") val workerJobPostingResponses: List<CrawlingJobPostingResponse>,
) {
    fun toVO(): List<CrawlingJobPosting> =
        workerJobPostingResponses.map { it.toVO() }
}