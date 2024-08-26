package com.idle.network.source.jobposting

import com.idle.network.api.JobPostingApi
import com.idle.network.model.jobposting.ApplyJobPostingRequest
import com.idle.network.model.jobposting.GetApplicantCountResponse
import com.idle.network.model.jobposting.GetApplicantsResponse
import com.idle.network.model.jobposting.GetCenterJobPostingDetailResponse
import com.idle.network.model.jobposting.GetJobPostingsCenterResponse
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

    suspend fun getJobPostingsApplied(next: String?, limit: Int): Result<GetJobPostingsResponse> =
        jobPostingApi.getJobPostingsApplied(next = next, limit = limit).onResponse()

    suspend fun getMyFavoritesJobPostings(
        next: String?,
        limit: Int
    ): Result<GetJobPostingsResponse> =
        jobPostingApi.getMyFavoritesJobPostings(next = next, limit = limit).onResponse()

    suspend fun getJobPostingsInProgress(): Result<GetJobPostingsCenterResponse> =
        jobPostingApi.getJobPostingsInProgress().onResponse()

    suspend fun getJobPostingsCompleted(): Result<GetJobPostingsCenterResponse> =
        jobPostingApi.getJobPostingsCompleted().onResponse()

    suspend fun getApplicantCount(jobPostingId: String): Result<GetApplicantCountResponse> =
        jobPostingApi.getApplicantCount(jobPostingId).onResponse()

    suspend fun applyJobPosting(applyJobPostingRequest: ApplyJobPostingRequest): Result<Unit> =
        jobPostingApi.applyJobPosting(applyJobPostingRequest).onResponse()

    suspend fun addFavoriteJobPosting(jobPostingId: String): Result<Unit> =
        jobPostingApi.addFavoriteJobPosting(jobPostingId).onResponse()

    suspend fun removeFavoriteJobPosting(jobPostingId: String): Result<Unit> =
        jobPostingApi.removeFavoriteJobPosting(jobPostingId).onResponse()

    suspend fun getApplicants(jobPostingId: String): Result<GetApplicantsResponse> =
        jobPostingApi.getApplicants(jobPostingId).onResponse()

    suspend fun endJobPosting(jobPostingId: String): Result<Unit> =
        jobPostingApi.endJobPosting(jobPostingId).onResponse()

    suspend fun deleteJobPosting(jobPostingId: String): Result<Unit> =
        jobPostingApi.deleteJobPosting(jobPostingId).onResponse()
}