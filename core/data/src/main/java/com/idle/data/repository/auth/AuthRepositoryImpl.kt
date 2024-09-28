package com.idle.data.repository.auth

import com.idle.datastore.datasource.TokenDataSource
import com.idle.datastore.datasource.UserInfoDataSource
import com.idle.domain.model.auth.BusinessRegistrationInfo
import com.idle.domain.model.auth.UserType
import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.domain.repositorry.auth.TokenRepository
import com.idle.network.model.auth.ConfirmAuthCodeRequest
import com.idle.network.model.auth.GenerateNewPasswordRequest
import com.idle.network.model.auth.SendPhoneRequest
import com.idle.network.model.auth.SignInCenterRequest
import com.idle.network.model.auth.SignInWorkerRequest
import com.idle.network.model.auth.SignUpCenterRequest
import com.idle.network.model.auth.SignUpWorkerRequest
import com.idle.network.model.auth.WithdrawalCenterRequest
import com.idle.network.model.auth.WithdrawalWorkerRequest
import com.idle.network.model.token.TokenResponse
import com.idle.network.source.auth.AuthDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val tokenDataSource: TokenDataSource,
    private val userInfoDataSource: UserInfoDataSource,
    private val tokenRepository: TokenRepository,
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
            SignInCenterRequest(identifier = identifier, password = password)
        ).fold(
            onSuccess = { tokenResponse ->
                handleSignInSuccess(tokenResponse, UserType.CENTER.apiValue)
                Result.success(Unit)
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
            handleSignInSuccess(tokenResponse, UserType.WORKER.apiValue)
            Result.success(Unit)
        },
        onFailure = { Result.failure(it) }
    )

    override suspend fun signInWorker(
        phoneNumber: String,
        authCode: String,
    ): Result<Unit> = authDataSource.signInWorker(
        SignInWorkerRequest(phoneNumber = phoneNumber, authCode = authCode)
    ).fold(
        onSuccess = { tokenResponse ->
            handleSignInSuccess(tokenResponse, UserType.WORKER.apiValue)
            Result.success(Unit)
        },
        onFailure = { Result.failure(it) }
    )

    override suspend fun logoutWorker(): Result<Unit> {
        return authDataSource.logoutWorker()
            .onSuccess { clearUserData() }
    }

    override suspend fun logoutCenter(): Result<Unit> = authDataSource.logoutCenter()
        .onSuccess { clearUserData() }

    override suspend fun withdrawalCenter(reason: String, password: String): Result<Unit> =
        authDataSource.withdrawalCenter(
            WithdrawalCenterRequest(reason = reason, password = password)
        ).onSuccess { clearUserData() }

    override suspend fun withdrawalWorker(reason: String): Result<Unit> =
        authDataSource.withdrawalWorker(WithdrawalWorkerRequest(reason))
            .onSuccess { clearUserData() }

    override suspend fun generateNewPassword(
        newPassword: String,
        phoneNumber: String
    ): Result<Unit> =
        authDataSource.generateNewPassword(
            GenerateNewPasswordRequest(
                newPassword = newPassword,
                phoneNumber = phoneNumber
            )
        )

    override suspend fun sendCenterVerificationRequest(): Result<Unit> =
        authDataSource.sendCenterVerificationRequest()

    private suspend fun handleSignInSuccess(
        tokenResponse: TokenResponse,
        userRole: String,
    ): Unit = withContext(Dispatchers.IO) {
        launch { tokenDataSource.setRefreshToken(tokenResponse.refreshToken) }
        launch { userInfoDataSource.setUserRole(userRole) }
        launch { tokenDataSource.setAccessToken(tokenResponse.accessToken) }

        val deviceToken = async { getDeviceToken() }
        tokenRepository.postDeviceToken(deviceToken.await())
    }

    private suspend fun clearUserData() = withContext(Dispatchers.IO) {
        launch { userInfoDataSource.clearUserRole() }
        launch { userInfoDataSource.clearUserInfo() }
        launch { tokenDataSource.clearToken() }

        tokenRepository.deleteDeviceToken()
    }

    private suspend fun getDeviceToken() = authDataSource.getDeviceToken()
}