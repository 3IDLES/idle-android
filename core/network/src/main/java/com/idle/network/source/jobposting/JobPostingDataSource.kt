package com.idle.network.source.jobposting

import com.idle.network.api.CareApi
import com.idle.network.model.jobposting.JobPostingRequest
import com.idle.network.util.onResponse
import javax.inject.Inject

class JobPostingDataSource @Inject constructor(
    private val careApi: CareApi
) {
    suspend fun postJobPosting(jobPostingRequest: JobPostingRequest): Result<Unit> =
        careApi.postJobPosting(jobPostingRequest).onResponse()

    suspend fun updateJobPosting(
        jobPostingId: String,
        jobPostingRequest: JobPostingRequest
    ): Result<Unit> = careApi.updateJobPosting(
        jobPostingId = jobPostingId,
        jobPostingRequest = jobPostingRequest
    ).onResponse()
}