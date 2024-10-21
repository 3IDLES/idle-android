package com.idle.network.source.jobposting

import com.idle.network.api.JobPostingApi
import com.idle.network.model.jobposting.ApplyJobPostingRequest
import com.idle.network.model.jobposting.FavoriteJobPostingRequest
import com.idle.network.model.jobposting.GetApplicantCountResponse
import com.idle.network.model.jobposting.GetApplicantsResponse
import com.idle.network.model.jobposting.GetCenterJobPostingDetailResponse
import com.idle.network.model.jobposting.GetCrawlingJobPostingDetailResponse
import com.idle.network.model.jobposting.GetCrawlingJobPostingsResponse
import com.idle.network.model.jobposting.GetFavoriteCrawlingJobPostingsResponse
import com.idle.network.model.jobposting.GetFavoriteJobPostingsResponse
import com.idle.network.model.jobposting.GetJobPostingsCenterResponse
import com.idle.network.model.jobposting.GetJobPostingsResponse
import com.idle.network.model.jobposting.GetWorkerJobPostingDetailResponse
import com.idle.network.model.jobposting.JobPostingRequest
import com.idle.network.util.safeApiCall
import javax.inject.Inject

class JobPostingDataSource @Inject constructor(
    private val jobPostingApi: JobPostingApi
) {
    suspend fun postJobPosting(jobPostingRequest: JobPostingRequest): Result<Unit> =
        safeApiCall { jobPostingApi.postJobPosting(jobPostingRequest) }

    suspend fun updateJobPosting(
        jobPostingId: String,
        jobPostingRequest: JobPostingRequest
    ): Result<Unit> = safeApiCall {
        jobPostingApi.updateJobPosting(
            jobPostingId = jobPostingId,
            jobPostingRequest = jobPostingRequest
        )
    }

    suspend fun getCenterJobPostingDetail(jobPostingId: String):
            Result<GetCenterJobPostingDetailResponse> = safeApiCall {
        jobPostingApi.getJobPostingDetailCenter(jobPostingId)
    }

    suspend fun getWorkerJobPostingDetail(jobPostingId: String):
            Result<GetWorkerJobPostingDetailResponse> = safeApiCall {
        jobPostingApi.getJobPostingDetailWorker(jobPostingId)
    }

    suspend fun getJobPostings(next: String?, limit: Int): Result<GetJobPostingsResponse> =
        safeApiCall { jobPostingApi.getJobPostings(next = next, limit = limit) }

    suspend fun getJobPostingsApplied(next: String?, limit: Int): Result<GetJobPostingsResponse> =
        safeApiCall { jobPostingApi.getJobPostingsApplied(next = next, limit = limit) }

    suspend fun getMyFavoriteJobPostings(): Result<GetFavoriteJobPostingsResponse> =
        safeApiCall { jobPostingApi.getMyFavoriteJobPostings() }

    suspend fun getMyFavoriteCrawlingJobPostings(): Result<GetFavoriteCrawlingJobPostingsResponse> =
        safeApiCall { jobPostingApi.getMyFavoriteCrawlingJobPostings() }

    suspend fun getJobPostingsInProgress(): Result<GetJobPostingsCenterResponse> =
        safeApiCall { jobPostingApi.getJobPostingsInProgress() }

    suspend fun getJobPostingsCompleted(): Result<GetJobPostingsCenterResponse> =
        safeApiCall { jobPostingApi.getJobPostingsCompleted() }

    suspend fun getApplicantCount(jobPostingId: String): Result<GetApplicantCountResponse> =
        safeApiCall { jobPostingApi.getApplicantCount(jobPostingId) }

    suspend fun applyJobPosting(applyJobPostingRequest: ApplyJobPostingRequest): Result<Unit> =
        safeApiCall { jobPostingApi.applyJobPosting(applyJobPostingRequest) }

    suspend fun addFavoriteJobPosting(
        jobPostingId: String,
        favoriteJobPostingRequest: FavoriteJobPostingRequest,
    ): Result<Unit> = safeApiCall {
        jobPostingApi.addFavoriteJobPosting(
            jobPostingId = jobPostingId,
            favoriteJobPostingRequest = favoriteJobPostingRequest
        )
    }

    suspend fun removeFavoriteJobPosting(jobPostingId: String): Result<Unit> =
        safeApiCall { jobPostingApi.removeFavoriteJobPosting(jobPostingId) }

    suspend fun getApplicants(jobPostingId: String): Result<GetApplicantsResponse> =
        safeApiCall { jobPostingApi.getApplicants(jobPostingId) }

    suspend fun endJobPosting(jobPostingId: String): Result<Unit> =
        safeApiCall { jobPostingApi.endJobPosting(jobPostingId) }

    suspend fun deleteJobPosting(jobPostingId: String): Result<Unit> =
        safeApiCall { jobPostingApi.deleteJobPosting(jobPostingId) }

    suspend fun getCrawlingJobPostings(
        next: String?,
        limit: Int
    ): Result<GetCrawlingJobPostingsResponse> =
        safeApiCall { jobPostingApi.getCrawlingJobPostings(next = next, limit = limit) }

    suspend fun getCrawlingJobPostingsDetail(
        jobPostingId: String
    ): Result<GetCrawlingJobPostingDetailResponse> =
        safeApiCall { jobPostingApi.getCrawlingJobPostingsDetail(jobPostingId) }
}
