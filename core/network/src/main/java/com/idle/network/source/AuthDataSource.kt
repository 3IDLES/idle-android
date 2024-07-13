package com.idle.network.source

import com.idle.network.api.CareNetworkApi
import com.idle.network.model.auth.BusinessRegistrationResponse
import com.idle.network.model.auth.ConfirmAuthCodeRequest
import com.idle.network.model.auth.RefreshTokenRequest
import com.idle.network.model.auth.SendPhoneRequest
import com.idle.network.model.auth.SignInCenterRequest
import com.idle.network.model.auth.SignUpCenterRequest
import com.idle.network.model.token.TokenResponse
import com.idle.network.util.onResponse
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val careNetworkApi: CareNetworkApi
) {
    suspend fun sendPhoneNumber(sendPhoneRequest: SendPhoneRequest): Result<Unit> =
        careNetworkApi.sendPhoneNumber(sendPhoneRequest).onResponse()

    suspend fun confirmAuthCode(confirmAuthCodeRequest: ConfirmAuthCodeRequest): Result<Unit> =
        careNetworkApi.confirmAuthCode(confirmAuthCodeRequest).onResponse()

    suspend fun signUpCenter(signUpCenterRequest: SignUpCenterRequest): Result<Unit> =
        careNetworkApi.signUpCenter(signUpCenterRequest).onResponse()

    suspend fun signInCenter(signInCenterRequest: SignInCenterRequest): Result<TokenResponse> =
        careNetworkApi.signInCenter(signInCenterRequest).onResponse()

    suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): Result<TokenResponse> =
        careNetworkApi.refreshToken(refreshTokenRequest).onResponse()

    suspend fun validateIdentifier(identifier: String): Result<Unit> =
        careNetworkApi.validateIdentifier(identifier).onResponse()

    suspend fun validateBusinessRegistrationNumber(
        businessRegistrationNumber: String
    ): Result<BusinessRegistrationResponse> =
        careNetworkApi.validateBusinessRegistrationNumber(businessRegistrationNumber).onResponse()
}