package com.idle.data.repository

import android.util.Log
import com.idle.datastore.datasource.TokenDataSource
import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.network.model.auth.ConfirmAuthCodeRequest
import com.idle.network.model.auth.SendPhoneRequest
import com.idle.network.model.auth.SignInCenterRequest
import com.idle.network.model.auth.SignUpCenterRequest
import com.idle.network.source.AuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val tokenDataSource: TokenDataSource,
) : AuthRepository {
    override suspend fun sendPhoneNumber(phoneNumber: String): Result<Unit> =
        authDataSource.sendPhoneNumber(SendPhoneRequest(phoneNumber))

    override suspend fun confirmAuthCode(
        phoneNumber: String,
        authCode: String,
    ): Result<Unit> =
        authDataSource.confirmAuthCode(
            ConfirmAuthCodeRequest(
                phoneNumber = phoneNumber,
                authCode = authCode,
            )
        )

    override suspend fun signUpCenter(
        identifier: String,
        password: String,
        phoneNumber: String,
        managerName: String,
        businessRegistrationNumber: String
    ): Result<Unit> = authDataSource.signUpCenter(
        SignUpCenterRequest(
            identifier = identifier,
            password = password,
            phoneNumber = phoneNumber,
            managerName = managerName,
            businessRegistrationNumber = businessRegistrationNumber,
        )
    )

    override suspend fun signInCenter(identifier: String, password: String): Result<Unit> =
        runCatching {
            authDataSource.signInCenter(
                SignInCenterRequest(
                    identifier = identifier,
                    password = password,
                )
            ).onSuccess { tokenResponse ->
                tokenDataSource.setAccessToken(tokenResponse.accessToken)
                tokenDataSource.setRefreshToken(tokenResponse.refreshToken)
            }.onFailure { Log.d("test", "센터 로그인 실패" + it.toString()) }
        }

    override suspend fun validateIdentifier(identifier: String): Result<Unit> =
        authDataSource.validateIdentifier(identifier)
}