package com.idle.network.source.jobposting

import com.idle.network.api.CareApi
import com.idle.network.model.jobposting.GetCenterJobPostingDetailResponse
import com.idle.network.model.jobposting.GetJobPostingsInProgressResponse
import com.idle.network.model.jobposting.GetJobPostingsResponse
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

    suspend fun getCenterJobPostingDetail(jobPostingId: String):
            Result<GetCenterJobPostingDetailResponse> =
        careApi.getJobPostingDetailCenter(jobPostingId).onResponse()

    suspend fun getJobPostings(next: String?, limit: Int): Result<GetJobPostingsResponse> =
        careApi.getJobPostings(next = next, limit = limit).onResponse()

    suspend fun getJobPostingsInProgress(): Result<GetJobPostingsInProgressResponse> =
        careApi.getJobPostingsInProgress().onResponse()
}