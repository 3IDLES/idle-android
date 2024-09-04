package com.idle.network.api

import com.idle.network.model.profile.CallbackImageUploadRequest
import com.idle.network.model.profile.GetCenterProfileResponse
import com.idle.network.model.profile.GetWorkerProfileResponse
import com.idle.network.model.profile.RegisterCenterProfileRequest
import com.idle.network.model.profile.UpdateCenterProfileRequest
import com.idle.network.model.profile.UpdateWorkerProfileRequest
import com.idle.network.model.profile.UploadProfileImageUrlResponse
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

interface UserApi {
    @GET("/api/v1/users/center/my/profile")
    suspend fun getMyCenterProfile(): Response<GetCenterProfileResponse>

    @PATCH("/api/v1/users/center/my/profile")
    suspend fun updateMyCenterProfile(
        @Body updateCenterProfileRequest: UpdateCenterProfileRequest
    ): Response<Unit>

    @GET("/api/v1/users/center/profile/{center-id}")
    suspend fun getCenterProfile(
        @Path("center-id") centerId: String,
    ): Response<GetCenterProfileResponse>

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
}