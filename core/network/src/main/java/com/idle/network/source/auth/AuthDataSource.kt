package com.idle.network.source.auth

import com.idle.network.api.AuthApi
import com.idle.network.model.auth.BusinessRegistrationResponse
import com.idle.network.model.auth.ConfirmAuthCodeRequest
import com.idle.network.model.auth.SendPhoneRequest
import com.idle.network.model.auth.SignInCenterRequest
import com.idle.network.model.auth.SignInWorkerRequest
import com.idle.network.model.auth.SignUpCenterRequest
import com.idle.network.model.auth.SignUpWorkerRequest
import com.idle.network.model.auth.WithdrawalCenterRequest
import com.idle.network.model.auth.WithdrawalWorkerRequest
import com.idle.network.model.token.TokenResponse
import com.idle.network.util.onResponse
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val authApi: AuthApi
) {
    suspend fun sendPhoneNumber(sendPhoneRequest: SendPhoneRequest): Result<Unit> =
        authApi.sendPhoneNumber(sendPhoneRequest).onResponse()

    suspend fun confirmAuthCode(confirmAuthCodeRequest: ConfirmAuthCodeRequest): Result<Unit> =
        authApi.confirmAuthCode(confirmAuthCodeRequest).onResponse()

    suspend fun signUpCenter(signUpCenterRequest: SignUpCenterRequest): Result<Unit> =
        authApi.signUpCenter(signUpCenterRequest).onResponse()

    suspend fun signInCenter(signInCenterRequest: SignInCenterRequest): Result<TokenResponse> =
        authApi.signInCenter(signInCenterRequest).onResponse()

    suspend fun signUpWorker(signUpWorkerRequest: SignUpWorkerRequest): Result<TokenResponse> =
        authApi.signUpWorker(signUpWorkerRequest).onResponse()

    suspend fun signInWorker(signInWorkerRequest: SignInWorkerRequest): Result<TokenResponse> =
        authApi.signInWorker(signInWorkerRequest).onResponse()

    suspend fun logoutWorker(): Result<Unit> = authApi.logoutWorker().onResponse()

    suspend fun logoutCenter(): Result<Unit> = authApi.logoutCenter().onResponse()

    suspend fun withdrawalCenter(withdrawalCenterRequest: WithdrawalCenterRequest): Result<Unit> =
        authApi.withdrawalCenter(withdrawalCenterRequest).onResponse()

    suspend fun withdrawalWorker(withdrawalWorkerRequest: WithdrawalWorkerRequest): Result<Unit> =
        authApi.withdrawalWorker(withdrawalWorkerRequest).onResponse()

    suspend fun validateIdentifier(identifier: String): Result<Unit> =
        authApi.validateIdentifier(identifier).onResponse()

    suspend fun validateBusinessRegistrationNumber(
        businessRegistrationNumber: String
    ): Result<BusinessRegistrationResponse> =
        authApi.validateBusinessRegistrationNumber(businessRegistrationNumber).onResponse()
}