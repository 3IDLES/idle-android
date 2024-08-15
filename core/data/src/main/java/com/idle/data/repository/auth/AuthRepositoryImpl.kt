package com.idle.data.repository.auth

import com.idle.datastore.datasource.TokenDataSource
import com.idle.domain.model.auth.BusinessRegistrationInfo
import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.network.model.auth.ConfirmAuthCodeRequest
import com.idle.network.model.auth.SendPhoneRequest
import com.idle.network.model.auth.SignInCenterRequest
import com.idle.network.model.auth.SignInWorkerRequest
import com.idle.network.model.auth.SignUpCenterRequest
import com.idle.network.model.auth.SignUpWorkerRequest
import com.idle.network.model.auth.WithdrawalCenterRequest
import com.idle.network.model.auth.WithdrawalWorkerRequest
import com.idle.network.source.auth.AuthDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    ): Result<Unit> = authDataSource.confirmAuthCode(
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
        authDataSource.signInCenter(
            SignInCenterRequest(
                identifier = identifier,
                password = password
            )
        ).fold(
            onSuccess = { tokenResponse ->
                withContext(Dispatchers.IO) {
                    tokenDataSource.setAccessToken(tokenResponse.accessToken)
                    launch { tokenDataSource.setRefreshToken(tokenResponse.refreshToken) }
                    Result.success(Unit)
                }
            },
            onFailure = { Result.failure(it) }
        )


    override suspend fun validateIdentifier(identifier: String): Result<Unit> =
        authDataSource.validateIdentifier(identifier)

    override suspend fun validateBusinessRegistrationNumber(
        businessRegistrationNumber: String,
    ): Result<BusinessRegistrationInfo> =
        authDataSource.validateBusinessRegistrationNumber(businessRegistrationNumber)
            .mapCatching { it.toVO() }

    override suspend fun signUpWorker(
        name: String,
        birthYear: Int,
        genderType: String,
        phoneNumber: String,
        roadNameAddress: String,
        lotNumberAddress: String,
    ): Result<Unit> = authDataSource.signUpWorker(
        SignUpWorkerRequest(
            name = name,
            birthYear = birthYear,
            genderType = genderType,
            phoneNumber = phoneNumber,
            roadNameAddress = roadNameAddress,
            lotNumberAddress = lotNumberAddress,
        )
    ).fold(
        onSuccess = { tokenResponse ->
            withContext(Dispatchers.IO) {
                tokenDataSource.setAccessToken(tokenResponse.accessToken)
                launch { tokenDataSource.setRefreshToken(tokenResponse.refreshToken) }
                Result.success(Unit)
            }
        },
        onFailure = { Result.failure(it) }
    )

    override suspend fun signInWorker(
        phoneNumber: String,
        authCode: String,
    ): Result<Unit> = authDataSource.signInWorker(
        SignInWorkerRequest(
            phoneNumber = phoneNumber,
            authCode = authCode,
        )
    )

    override suspend fun logoutWorker(): Result<Unit> = authDataSource.logoutWorker()
        .fold(
            onSuccess = {
                tokenDataSource.clearToken()
                return Result.success(Unit)
            },
            onFailure = { Result.failure(it) }
        )

    override suspend fun logoutCenter(): Result<Unit> = authDataSource.logoutCenter()
        .fold(
            onSuccess = {
                tokenDataSource.clearToken()
                return Result.success(Unit)
            },
            onFailure = { Result.failure(it) }
        )

    override suspend fun withdrawalCenter(reason: String, password: String): Result<Unit> =
        authDataSource.withdrawalCenter(
            WithdrawalCenterRequest(
                reason = reason,
                password = password
            )
        ).fold(
            onSuccess = {
                tokenDataSource.clearToken()
                return Result.success(Unit)
            },
            onFailure = { Result.failure(it) }
        )

    override suspend fun withdrawalWorker(reason: String): Result<Unit> =
        authDataSource.withdrawalWorker(WithdrawalWorkerRequest(reason))
            .fold(
                onSuccess = {
                    tokenDataSource.clearToken()
                    return Result.success(Unit)
                },
                onFailure = { Result.failure(it) }
            )
}