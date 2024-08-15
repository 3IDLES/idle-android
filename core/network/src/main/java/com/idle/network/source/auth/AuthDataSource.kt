package com.idle.network.source.auth

import com.idle.network.api.CareApi
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
    private val careApi: CareApi
) {
    suspend fun sendPhoneNumber(sendPhoneRequest: SendPhoneRequest): Result<Unit> =
        careApi.sendPhoneNumber(sendPhoneRequest).onResponse()

    suspend fun confirmAuthCode(confirmAuthCodeRequest: ConfirmAuthCodeRequest): Result<Unit> =
        careApi.confirmAuthCode(confirmAuthCodeRequest).onResponse()

    suspend fun signUpCenter(signUpCenterRequest: SignUpCenterRequest): Result<Unit> =
        careApi.signUpCenter(signUpCenterRequest).onResponse()

    suspend fun signInCenter(signInCenterRequest: SignInCenterRequest): Result<TokenResponse> =
        careApi.signInCenter(signInCenterRequest).onResponse()

    suspend fun signUpWorker(signUpWorkerRequest: SignUpWorkerRequest): Result<TokenResponse> =
        careApi.signUpWorker(signUpWorkerRequest).onResponse()

    suspend fun signInWorker(signInWorkerRequest: SignInWorkerRequest): Result<Unit> =
        careApi.signInWorker(signInWorkerRequest).onResponse()

    suspend fun logoutWorker(): Result<Unit> = careApi.logoutWorker().onResponse()

    suspend fun logoutCenter(): Result<Unit> = careApi.logoutCenter().onResponse()

    suspend fun withdrawalCenter(withdrawalCenterRequest: WithdrawalCenterRequest): Result<Unit> =
        careApi.withdrawalCenter(withdrawalCenterRequest).onResponse()

    suspend fun withdrawalWorker(withdrawalWorkerRequest: WithdrawalWorkerRequest): Result<Unit> =
        careApi.withdrawalWorker(withdrawalWorkerRequest).onResponse()

    suspend fun validateIdentifier(identifier: String): Result<Unit> =
        careApi.validateIdentifier(identifier).onResponse()

    suspend fun validateBusinessRegistrationNumber(
        businessRegistrationNumber: String
    ): Result<BusinessRegistrationResponse> =
        careApi.validateBusinessRegistrationNumber(businessRegistrationNumber).onResponse()
}