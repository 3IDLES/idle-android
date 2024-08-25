package com.idle.network.api

import com.idle.network.model.jobposting.ApplyJobPostingRequest
import com.idle.network.model.jobposting.GetCenterJobPostingDetailResponse
import com.idle.network.model.jobposting.GetJobPostingsInProgressResponse
import com.idle.network.model.jobposting.GetJobPostingsResponse
import com.idle.network.model.jobposting.GetWorkerJobPostingDetailResponse
import com.idle.network.model.jobposting.JobPostingRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface JobPostingApi {
    @POST("/api/v1/job-postings")
    suspend fun postJobPosting(@Body jobPostingRequest: JobPostingRequest): Response<Unit>

    @PATCH("/api/v1/job-postings/{job-posting-id}")
    suspend fun updateJobPosting(
        @Path("job-posting-id") jobPostingId: String,
        @Body jobPostingRequest: JobPostingRequest,
    ): Response<Unit>

    @GET("/api/v1/job-postings/{job-posting-id}/center")
    suspend fun getJobPostingDetailCenter(
        @Path("job-posting-id") jobPostingId: String
    ): Response<GetCenterJobPostingDetailResponse>

    @GET("/api/v1/job-postings")
    suspend fun getJobPostings(
        @Query("next") next: String?,
        @Query("limit") limit: Int,
    ): Response<GetJobPostingsResponse>

    @GET("/api/v1/job-postings/status/in-progress")
    suspend fun getJobPostingsInProgress(): Response<GetJobPostingsInProgressResponse>

    @GET("/api/v1/job-postings/{job-posting-id}/carer")
    suspend fun getJobPostingDetailWorker(
        @Path("job-posting-id") jobPostingId: String
    ): Response<GetWorkerJobPostingDetailResponse>

    @POST("/api/v1/applys")
    suspend fun applyJobPosting(@Body applyJobPostingRequest: ApplyJobPostingRequest): Response<Unit>
}
