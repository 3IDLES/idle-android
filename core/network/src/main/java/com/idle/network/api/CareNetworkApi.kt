package com.idle.network.api

import com.idle.network.model.auth.BusinessRegistrationResponse
import com.idle.network.model.auth.ConfirmAuthCodeRequest
import com.idle.network.model.auth.SendPhoneRequest
import com.idle.network.model.auth.SignInCenterRequest
import com.idle.network.model.auth.SignUpCenterRequest
import com.idle.network.model.profile.CallbackImageUploadRequest
import com.idle.network.model.profile.CenterProfileRequest
import com.idle.network.model.profile.CenterProfileResponse
import com.idle.network.model.profile.ProfileImageUploadUrlResponse
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

interface CareNetworkApi {
    @POST("/api/v1/auth/common/send")
    suspend fun sendPhoneNumber(@Body sendPhoneRequest: SendPhoneRequest): Response<Unit>

    @POST("/api/v1/auth/common/confirm")
    suspend fun confirmAuthCode(@Body confirmAuthCodeRequest: ConfirmAuthCodeRequest): Response<Unit>

    @POST("/api/v1/auth/center/join")
    suspend fun signUpCenter(@Body signUpCenterRequest: SignUpCenterRequest): Response<Unit>

    @POST("/api/v1/auth/center/login")
    suspend fun signInCenter(@Body signInCenterRequest: SignInCenterRequest): Response<TokenResponse>

    @GET("/api/v1/auth/center/validation/{identifier}")
    suspend fun validateIdentifier(@Path("identifier") identifier: String): Response<Unit>

    @GET("/api/v1/auth/center/authentication/{businessRegistrationNumber}")
    suspend fun validateBusinessRegistrationNumber(
        @Path("businessRegistrationNumber") businessRegistrationNumber: String
    ): Response<BusinessRegistrationResponse>

    @GET("/api/v1/users/center/my/profile")
    suspend fun getMyCenterProfile(): Response<CenterProfileResponse>

    @PATCH("/api/v1/users/center/my/profile")
    suspend fun updateMyCenterProfile(
        @Body centerProfileRequest: CenterProfileRequest
    ): Response<Unit>

    @GET("/api/v1/users/{user-type}/my/profile-image/upload-url")
    suspend fun getImageUploadUrl(
        @Path("user-type") userType: String,
        @Query("imageFileExtension") imageFileExtension: String,
    ): Response<ProfileImageUploadUrlResponse>

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
}