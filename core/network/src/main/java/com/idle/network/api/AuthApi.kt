package com.idle.network.api

import com.idle.network.model.auth.BusinessRegistrationResponse
import com.idle.network.model.auth.ConfirmAuthCodeRequest
import com.idle.network.model.auth.GenerateNewPasswordRequest
import com.idle.network.model.auth.FCMTokenRequest
import com.idle.network.model.auth.SendPhoneRequest
import com.idle.network.model.auth.SignInCenterRequest
import com.idle.network.model.auth.SignInWorkerRequest
import com.idle.network.model.auth.SignUpCenterRequest
import com.idle.network.model.auth.SignUpWorkerRequest
import com.idle.network.model.auth.WithdrawalCenterRequest
import com.idle.network.model.auth.WithdrawalWorkerRequest
import com.idle.network.model.token.RefreshTokenRequest
import com.idle.network.model.token.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {
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
    suspend fun signInWorker(@Body signInWorkerRequest: SignInWorkerRequest): Response<TokenResponse>

    @POST("/api/v1/auth/center/logout")
    suspend fun logoutCenter(): Response<Unit>

    @POST("/api/v1/auth/carer/logout")
    suspend fun logoutWorker(): Response<Unit>

    @POST("/api/v1/auth/center/withdraw")
    suspend fun withdrawalCenter(
        @Body withdrawalCenterRequest: WithdrawalCenterRequest
    ): Response<Unit>

    @POST("/api/v1/auth/carer/withdraw")
    suspend fun withdrawalWorker(
        @Body withdrawalWorkerRequest: WithdrawalWorkerRequest
    ): Response<Unit>

    @GET("/api/v1/auth/center/validation/{identifier}")
    suspend fun validateIdentifier(@Path("identifier") identifier: String): Response<Unit>

    @GET("/api/v1/auth/center/authentication/{businessRegistrationNumber}")
    suspend fun validateBusinessRegistrationNumber(
        @Path("businessRegistrationNumber") businessRegistrationNumber: String
    ): Response<BusinessRegistrationResponse>

    @PATCH("/api/v1/auth/center/password/new")
    suspend fun generateNewPassword(
        @Body generateNewPasswordRequest: GenerateNewPasswordRequest
    ): Response<Unit>

    @PATCH("/api/v1/auth/center/join/verify")
    suspend fun sendCenterVerificationRequest(): Response<Unit>
}