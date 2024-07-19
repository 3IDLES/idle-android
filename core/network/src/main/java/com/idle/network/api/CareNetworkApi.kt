package com.idle.network.api

import com.idle.network.model.auth.BusinessRegistrationResponse
import com.idle.network.model.auth.ConfirmAuthCodeRequest
import com.idle.network.model.auth.SendPhoneRequest
import com.idle.network.model.auth.SignInCenterRequest
import com.idle.network.model.auth.SignUpCenterRequest
import com.idle.network.model.profile.CenterProfileRequest
import com.idle.network.model.profile.CenterProfileResponse
import com.idle.network.model.token.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

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
}