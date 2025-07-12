package com.idle.data.repository

import com.idle.datastore.datasource.TokenDataSource
import com.idle.datastore.datasource.UserInfoDataSource
import com.idle.domain.model.auth.BusinessRegistrationInfo
import com.idle.domain.model.auth.UserType
import com.idle.domain.repositorry.AuthRepository
import com.idle.domain.repositorry.ProfileRepository
import com.idle.domain.repositorry.TokenRepository
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
import com.idle.network.source.AuthDataSource
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
    override suspend fun sendPhoneNumber(phoneNumber: String) {
        authDataSource.sendPhoneNumber(SendPhoneRequest(phoneNumber))
    }

    override suspend fun confirmAuthCode(
        phoneNumber: String,
        authCode: String,
    ) {
        authDataSource.confirmAuthCode(
            ConfirmAuthCodeRequest(
                phoneNumber = phoneNumber,
                authCode = authCode,
            )
        )
    }

    override suspend fun signUpCenter(
        identifier: String,
        password: String,
        phoneNumber: String,
        managerName: String,
        businessRegistrationNumber: String
    ) {
        authDataSource.signUpCenter(
            SignUpCenterRequest(
                identifier = identifier,
                password = password,
                phoneNumber = phoneNumber,
                managerName = managerName,
                businessRegistrationNumber = businessRegistrationNumber,
            )
        )
    }

    override suspend fun signInCenter(identifier: String, password: String) {
        val tokenResponse: TokenResponse = authDataSource.signInCenter(
            SignInCenterRequest(identifier = identifier, password = password)
        )
        coroutineScope {
            handleSignInSuccess(tokenResponse, UserType.CENTER.apiValue)
            val profile = profileRepository.getMyCenterProfile()
            userInfoDataSource.setUserInfo(profile.toString())
        }
    }

    override suspend fun validateIdentifier(identifier: String) {
        authDataSource.validateIdentifier(identifier)
    }

    override suspend fun validateBusinessRegistrationNumber(
        businessRegistrationNumber: String,
    ): BusinessRegistrationInfo =
        authDataSource.validateBusinessRegistrationNumber(businessRegistrationNumber)
            .toVO()

    override suspend fun signUpWorker(
        name: String,
        birthYear: Int,
        genderType: String,
        phoneNumber: String,
        roadNameAddress: String,
        lotNumberAddress: String,
    ) {
        val tokenResponse: TokenResponse = authDataSource.signUpWorker(
            SignUpWorkerRequest(
                name = name,
                birthYear = birthYear,
                genderType = genderType,
                phoneNumber = phoneNumber,
                roadNameAddress = roadNameAddress,
                lotNumberAddress = lotNumberAddress,
            )
        )
        coroutineScope {
            handleSignInSuccess(tokenResponse, UserType.WORKER.apiValue)
            val profile = profileRepository.getMyWorkerProfile()
            userInfoDataSource.setUserInfo(profile.toString())
        }
    }

    override suspend fun signInWorker(
        phoneNumber: String,
        authCode: String,
    ) {
        val tokenResponse: TokenResponse = authDataSource.signInWorker(
            SignInWorkerRequest(phoneNumber = phoneNumber, authCode = authCode)
        )
        coroutineScope {
            handleSignInSuccess(tokenResponse, UserType.WORKER.apiValue)
            val updatedProfile = profileRepository.getMyWorkerProfile()
            userInfoDataSource.setUserInfo(updatedProfile.toString())
        }
    }

    override suspend fun logoutWorker() {
        tokenRepository.deleteDeviceToken(getDeviceToken())
        authDataSource.logoutWorker()
        clearUserData()
    }

    override suspend fun logoutCenter() {
        tokenRepository.deleteDeviceToken(getDeviceToken())
        authDataSource.logoutCenter()
        clearUserData()
    }

    override suspend fun withdrawalCenter(reason: String, password: String) {
        tokenRepository.deleteDeviceToken(getDeviceToken())
        authDataSource.withdrawalCenter(
            WithdrawalCenterRequest(reason = reason, password = password)
        )
        clearUserData()
    }

    override suspend fun withdrawalWorker(reason: String) {
        tokenRepository.deleteDeviceToken(getDeviceToken())
        authDataSource.withdrawalWorker(WithdrawalWorkerRequest(reason))
        clearUserData()
    }

    override suspend fun generateNewPassword(
        newPassword: String,
        phoneNumber: String
    ) {
        authDataSource.generateNewPassword(
            GenerateNewPasswordRequest(
                newPassword = newPassword,
                phoneNumber = phoneNumber
            )
        )
    }

    override suspend fun sendCenterVerificationRequest() {
        authDataSource.sendCenterVerificationRequest()
    }

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
