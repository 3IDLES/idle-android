package com.idle.data.repository.auth

import com.idle.datastore.datasource.TokenDataSource
import com.idle.datastore.datasource.UserInfoDataSource
import com.idle.domain.model.auth.BusinessRegistrationInfo
import com.idle.domain.model.auth.UserType
import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.domain.repositorry.auth.TokenRepository
import com.idle.domain.repositorry.profile.ProfileRepository
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
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
                coroutineScope {
                    handleSignInSuccess(tokenResponse, UserType.CENTER.apiValue)

                    val profile = profileRepository.getMyCenterProfile().getOrNull()
                    if (profile != null) {
                        userInfoDataSource.setUserInfo(profile.toString())
                    }

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
            coroutineScope {
                handleSignInSuccess(tokenResponse, UserType.WORKER.apiValue)

                val profile = profileRepository.getMyWorkerProfile().getOrNull()
                if (profile != null) {
                    userInfoDataSource.setUserInfo(profile.toString())
                }

                Result.success(Unit)
            }
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
            coroutineScope {
                handleSignInSuccess(tokenResponse, UserType.WORKER.apiValue)

                val updatedProfile = profileRepository.getMyWorkerProfile().getOrNull()
                if (updatedProfile != null) {
                    userInfoDataSource.setUserInfo(updatedProfile.toString())
                }

                Result.success(Unit)
            }
        },
        onFailure = { Result.failure(it) }
    )

    override suspend fun logoutWorker(): Result<Unit> {
        tokenRepository.deleteDeviceToken(getDeviceToken())

        return authDataSource.logoutWorker()
            .onSuccess { clearUserData() }
    }

    override suspend fun logoutCenter(): Result<Unit> {
        tokenRepository.deleteDeviceToken(getDeviceToken())

        return authDataSource.logoutCenter()
            .onSuccess { clearUserData() }
    }

    override suspend fun withdrawalCenter(reason: String, password: String): Result<Unit> {
        tokenRepository.deleteDeviceToken(getDeviceToken())

        return authDataSource.withdrawalCenter(
            WithdrawalCenterRequest(reason = reason, password = password)
        ).onSuccess { clearUserData() }
    }

    override suspend fun withdrawalWorker(reason: String): Result<Unit> {
        tokenRepository.deleteDeviceToken(getDeviceToken())

        return authDataSource.withdrawalWorker(WithdrawalWorkerRequest(reason))
            .onSuccess { clearUserData() }
    }

    override suspend fun generateNewPassword(
        newPassword: String,
        phoneNumber: String
    ): Result<Unit> = authDataSource.generateNewPassword(
        GenerateNewPasswordRequest(
            newPassword = newPassword,
            phoneNumber = phoneNumber
        )
    )

    override suspend fun sendCenterVerificationRequest(): Result<Unit> =
        authDataSource.sendCenterVerificationRequest()

    private suspend fun handleSignInSuccess(
        tokenResponse: TokenResponse,
        userType: String,
    ) = coroutineScope {
        launch { tokenDataSource.setRefreshToken(tokenResponse.refreshToken) }
        launch { userInfoDataSource.setUserType(userType) }
        launch { tokenDataSource.setAccessToken(tokenResponse.accessToken) }.join()

        val deviceToken = getDeviceToken()
        tokenRepository.postDeviceToken(deviceToken, userType = userType)
    }

    private suspend fun clearUserData() = coroutineScope {
        launch { userInfoDataSource.clearUserType() }
        launch { userInfoDataSource.clearUserInfo() }
        tokenDataSource.clearToken()
    }

    private suspend fun getDeviceToken() = authDataSource.getDeviceToken()
}
