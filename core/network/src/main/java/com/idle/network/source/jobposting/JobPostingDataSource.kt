package com.idle.network.source.jobposting

import com.idle.network.api.CareNetworkApi
import com.idle.network.model.jobposting.JobPostingRequest
import com.idle.network.util.onResponse
import javax.inject.Inject

class JobPostingDataSource @Inject constructor(
    private val careNetworkApi: CareNetworkApi
) {
    suspend fun postJobPosting(jobPostingRequest: JobPostingRequest): Result<Unit> =
        careNetworkApi.postJobPosting(jobPostingRequest).onResponse()

    suspend fun updateJobPosting(
        jobPostingId: String,
        jobPostingRequest: JobPostingRequest
    ): Result<Unit> = careNetworkApi.updateJobPosting(
        jobPostingId = jobPostingId,
        jobPostingRequest = jobPostingRequest
    ).onResponse()
}