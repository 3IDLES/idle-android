package com.idle.network.source

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
import com.idle.network.util.onResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobPostingDataSource @Inject constructor(
    private val jobPostingApi: JobPostingApi
) {
    suspend fun postJobPosting(jobPostingRequest: JobPostingRequest): Unit =
        jobPostingApi.postJobPosting(jobPostingRequest).onResponse()

    suspend fun updateJobPosting(
        jobPostingId: String,
        jobPostingRequest: JobPostingRequest
    ): Unit =
        jobPostingApi.updateJobPosting(
            jobPostingId = jobPostingId,
            jobPostingRequest = jobPostingRequest
        ).onResponse()

    suspend fun getCenterJobPostingDetail(jobPostingId: String): GetCenterJobPostingDetailResponse =
        jobPostingApi.getJobPostingDetailCenter(jobPostingId).onResponse()

    suspend fun getWorkerJobPostingDetail(jobPostingId: String): GetWorkerJobPostingDetailResponse =
        jobPostingApi.getJobPostingDetailWorker(jobPostingId).onResponse()

    suspend fun getJobPostings(next: String?, limit: Int): GetJobPostingsResponse =
        jobPostingApi.getJobPostings(next = next, limit = limit).onResponse()

    suspend fun getJobPostingsApplied(next: String?, limit: Int): GetJobPostingsResponse =
        jobPostingApi.getJobPostingsApplied(next = next, limit = limit).onResponse()

    suspend fun getMyFavoriteJobPostings(): GetFavoriteJobPostingsResponse =
        jobPostingApi.getMyFavoriteJobPostings().onResponse()

    suspend fun getMyFavoriteCrawlingJobPostings(): GetFavoriteCrawlingJobPostingsResponse =
        jobPostingApi.getMyFavoriteCrawlingJobPostings().onResponse()

    suspend fun getJobPostingsInProgress(): GetJobPostingsCenterResponse =
        jobPostingApi.getJobPostingsInProgress().onResponse()

    suspend fun getJobPostingsCompleted(): GetJobPostingsCenterResponse =
        jobPostingApi.getJobPostingsCompleted().onResponse()

    suspend fun getApplicantCount(jobPostingId: String): GetApplicantCountResponse =
        jobPostingApi.getApplicantCount(jobPostingId).onResponse()

    suspend fun applyJobPosting(applyJobPostingRequest: ApplyJobPostingRequest): Unit =
        jobPostingApi.applyJobPosting(applyJobPostingRequest).onResponse()

    suspend fun addFavoriteJobPosting(
        jobPostingId: String,
        favoriteJobPostingRequest: FavoriteJobPostingRequest,
    ): Unit =
        jobPostingApi.addFavoriteJobPosting(
            jobPostingId = jobPostingId,
            favoriteJobPostingRequest = favoriteJobPostingRequest
        ).onResponse()

    suspend fun removeFavoriteJobPosting(jobPostingId: String): Unit =
        jobPostingApi.removeFavoriteJobPosting(jobPostingId).onResponse()

    suspend fun getApplicants(jobPostingId: String): GetApplicantsResponse =
        jobPostingApi.getApplicants(jobPostingId).onResponse()

    suspend fun endJobPosting(jobPostingId: String): Unit =
        jobPostingApi.endJobPosting(jobPostingId).onResponse()

    suspend fun deleteJobPosting(jobPostingId: String): Unit =
        jobPostingApi.deleteJobPosting(jobPostingId).onResponse()

    suspend fun getCrawlingJobPostings(
        next: String?,
        limit: Int,
        distance: Int,
    ): GetCrawlingJobPostingsResponse =
        jobPostingApi.getCrawlingJobPostings(next = next, limit = limit, distance = distance)
            .onResponse()

    suspend fun getCrawlingJobPostingsDetail(
        jobPostingId: String
    ): GetCrawlingJobPostingDetailResponse =
        jobPostingApi.getCrawlingJobPostingsDetail(jobPostingId).onResponse()
}
