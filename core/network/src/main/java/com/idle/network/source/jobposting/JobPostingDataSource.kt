package com.idle.network.source.jobposting

import com.idle.network.api.JobPostingApi
import com.idle.network.model.jobposting.ApplyJobPostingRequest
import com.idle.network.model.jobposting.GetCenterJobPostingDetailResponse
import com.idle.network.model.jobposting.GetJobPostingsInProgressResponse
import com.idle.network.model.jobposting.GetJobPostingsResponse
import com.idle.network.model.jobposting.GetWorkerJobPostingDetailResponse
import com.idle.network.model.jobposting.JobPostingRequest
import com.idle.network.util.onResponse
import javax.inject.Inject

class JobPostingDataSource @Inject constructor(
    private val jobPostingApi: JobPostingApi
) {
    suspend fun postJobPosting(jobPostingRequest: JobPostingRequest): Result<Unit> =
        jobPostingApi.postJobPosting(jobPostingRequest).onResponse()

    suspend fun updateJobPosting(
        jobPostingId: String,
        jobPostingRequest: JobPostingRequest
    ): Result<Unit> = jobPostingApi.updateJobPosting(
        jobPostingId = jobPostingId,
        jobPostingRequest = jobPostingRequest
    ).onResponse()

    suspend fun getCenterJobPostingDetail(jobPostingId: String):
            Result<GetCenterJobPostingDetailResponse> =
        jobPostingApi.getJobPostingDetailCenter(jobPostingId).onResponse()

    suspend fun getWorkerJobPostingDetail(jobPostingId: String):
            Result<GetWorkerJobPostingDetailResponse> =
        jobPostingApi.getJobPostingDetailWorker(jobPostingId).onResponse()

    suspend fun getJobPostings(next: String?, limit: Int): Result<GetJobPostingsResponse> =
        jobPostingApi.getJobPostings(next = next, limit = limit).onResponse()

    suspend fun getJobPostingsInProgress(): Result<GetJobPostingsInProgressResponse> =
        jobPostingApi.getJobPostingsInProgress().onResponse()

    suspend fun applyJobPosting(applyJobPostingRequest: ApplyJobPostingRequest): Result<Unit> =
        jobPostingApi.applyJobPosting(applyJobPostingRequest).onResponse()
}