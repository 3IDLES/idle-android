package com.idle.network.api

import com.idle.network.model.auth.BusinessRegistrationResponse
import com.idle.network.model.auth.ConfirmAuthCodeRequest
import com.idle.network.model.auth.SendPhoneRequest
import com.idle.network.model.auth.SignInCenterRequest
import com.idle.network.model.auth.SignInWorkerRequest
import com.idle.network.model.auth.SignUpCenterRequest
import com.idle.network.model.auth.SignUpWorkerRequest
import com.idle.network.model.auth.WithdrawalCenterRequest
import com.idle.network.model.auth.WithdrawalWorkerRequest
import com.idle.network.model.jobposting.GetCenterJobPostingDetailResponse
import com.idle.network.model.jobposting.GetJobPostingsResponse
import com.idle.network.model.jobposting.JobPostingRequest
import com.idle.network.model.profile.CallbackImageUploadRequest
import com.idle.network.model.profile.GetCenterProfileResponse
import com.idle.network.model.profile.GetWorkerProfileResponse
import com.idle.network.model.profile.RegisterCenterProfileRequest
import com.idle.network.model.profile.UpdateCenterProfileRequest
import com.idle.network.model.profile.UpdateWorkerProfileRequest
import com.idle.network.model.profile.UploadProfileImageUrlResponse
import com.idle.network.model.token.RefreshTokenRequest
import com.idle.network.model.token.TokenResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface CareApi {
    @POST("/api/v1/auth/common/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Response<TokenResponse>

    @POST("/api/v1/auth/common/send")
    suspend fun sendPhoneNumber(@Body sendPhoneRequest: SendPhoneRequest): Response<Unit>

    @POST("/api/v1/auth/common/confirm")
    suspend fun confirmAuthCode(@Body confirmAuthCodeRequest: ConfirmAuthCodeRequest): Response<Unit>

    @POST("/api/v1/auth/center/join")
    suspend fun signUpCenter(@Body signUpCenterRequest: SignUpCenterRequest): Response<Unit>

    @POST("/api/v1/auth/center/login")
    suspend fun signInCenter(@Body signInCenterRequest: SignInCenterRequest): Response<TokenResponse>

    @POST("/api/v1/auth/carer/join")
    suspend fun signUpWorker(@Body signUpWorkerRequest: SignUpWorkerRequest): Response<TokenResponse>

    @POST("/api/v1/auth/carer/login")
    suspend fun signInWorker(@Body signInWorkerRequest: SignInWorkerRequest): Response<Unit>

    @POST("/api/v1/auth/center/logout")
    suspend fun logoutCenter(): Response<Unit>

    @POST("/api/v1/auth/worker/logout")
    suspend fun logoutWorker(): Response<Unit>

    @POST("/api/v1/auth/center/withdraw")
    suspend fun withdrawalCenter(
        @Body withdrawalCenterResponse: WithdrawalCenterRequest
    ): Response<Unit>

    @POST("/api/v1/auth/worker/withdraw")
    suspend fun withdrawalWorker(
        @Body withdrawalWorkerResponse: WithdrawalWorkerRequest
    ): Response<Unit>

    @GET("/api/v1/auth/center/validation/{identifier}")
    suspend fun validateIdentifier(@Path("identifier") identifier: String): Response<Unit>

    @GET("/api/v1/auth/center/authentication/{businessRegistrationNumber}")
    suspend fun validateBusinessRegistrationNumber(
        @Path("businessRegistrationNumber") businessRegistrationNumber: String
    ): Response<BusinessRegistrationResponse>

    @GET("/api/v1/users/center/my/profile")
    suspend fun getMyCenterProfile(): Response<GetCenterProfileResponse>

    @PATCH("/api/v1/users/center/my/profile")
    suspend fun updateMyCenterProfile(
        @Body updateCenterProfileRequest: UpdateCenterProfileRequest
    ): Response<Unit>

    @GET("/api/v1/users/{user-type}/my/profile-image/upload-url")
    suspend fun getImageUploadUrl(
        @Path("user-type") userType: String,
        @Query("imageFileExtension") imageFileExtension: String,
    ): Response<UploadProfileImageUrlResponse>

    @PUT
    suspend fun uploadProfileImage(
        @Url uploadUrl: String,
        @Body requestImage: RequestBody,
    ): Response<Unit>

    @POST("/api/v1/users/{user-type}/my/profile-image/upload-callback")
    suspend fun callbackImageUpload(
        @Path("user-type") userType: String,
        @Body callbackImageUploadRequest: CallbackImageUploadRequest,
    ): Response<Unit>

    @GET("/api/v1/users/carer/my/profile")
    suspend fun getMyWorkerProfile(): Response<GetWorkerProfileResponse>

    @GET("/api/v1/users/carer/profile/{carer-id}")
    suspend fun getWorkerProfile(
        @Path("carer-id") workerId: String,
    ): Response<GetWorkerProfileResponse>

    @PATCH("/api/v1/users/carer/my/profile")
    suspend fun updateWorkerProfile(
        @Body updateWorkerProfileRequest: UpdateWorkerProfileRequest
    ): Response<Unit>

    @POST("/api/v1/users/center/my/profile")
    suspend fun registerCenterProfile(
        @Body registerCenterProfileRequest: RegisterCenterProfileRequest
    ): Response<Unit>

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
}