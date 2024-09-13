package com.idle.network.model.jobposting

import com.idle.domain.model.jobposting.WorkerJobPosting
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetFavoriteJobPostingsResponse(
    @SerialName("favoriteJobPostings") val workerJobPostingResponses: List<WorkerJobPostingResponse>,
) {
    fun toVO(): List<WorkerJobPosting> =
        workerJobPostingResponses.map { it.toVO() }
}