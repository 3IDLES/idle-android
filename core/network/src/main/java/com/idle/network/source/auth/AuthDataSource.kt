package com.idle.network.source.auth

import com.google.firebase.messaging.FirebaseMessaging
import com.idle.network.api.AuthApi
import com.idle.network.model.auth.BusinessRegistrationResponse
import com.idle.network.model.auth.ConfirmAuthCodeRequest
import com.idle.network.model.auth.FCMTokenRequest
import com.idle.network.model.auth.GenerateNewPasswordRequest
import com.idle.network.model.auth.SendPhoneRequest
import com.idle.network.model.auth.SignInCenterRequest
import com.idle.network.model.auth.SignInWorkerRequest
import com.idle.network.model.auth.SignUpCenterRequest
import com.idle.network.model.auth.SignUpWorkerRequest
import com.idle.network.model.auth.WithdrawalCenterRequest
import com.idle.network.model.auth.WithdrawalWorkerRequest
import com.idle.network.model.token.TokenResponse
import com.idle.network.util.safeApiCall
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthDataSource @Inject constructor(
    private val authApi: AuthApi,
    private val firebaseMessaging: FirebaseMessaging,
) {
    suspend fun sendPhoneNumber(sendPhoneRequest: SendPhoneRequest): Result<Unit> =
        safeApiCall { authApi.sendPhoneNumber(sendPhoneRequest) }

    suspend fun confirmAuthCode(confirmAuthCodeRequest: ConfirmAuthCodeRequest): Result<Unit> =
        safeApiCall { authApi.confirmAuthCode(confirmAuthCodeRequest) }

    suspend fun signUpCenter(signUpCenterRequest: SignUpCenterRequest): Result<Unit> =
        safeApiCall { authApi.signUpCenter(signUpCenterRequest) }

    suspend fun signInCenter(signInCenterRequest: SignInCenterRequest): Result<TokenResponse> =
        safeApiCall { authApi.signInCenter(signInCenterRequest) }

    suspend fun signUpWorker(signUpWorkerRequest: SignUpWorkerRequest): Result<TokenResponse> =
        safeApiCall { authApi.signUpWorker(signUpWorkerRequest) }

    suspend fun signInWorker(signInWorkerRequest: SignInWorkerRequest): Result<TokenResponse> =
        safeApiCall { authApi.signInWorker(signInWorkerRequest) }

    suspend fun logoutWorker(): Result<Unit> = safeApiCall { authApi.logoutWorker() }

    suspend fun logoutCenter(): Result<Unit> = safeApiCall { authApi.logoutCenter() }

    suspend fun withdrawalCenter(withdrawalCenterRequest: WithdrawalCenterRequest): Result<Unit> =
        safeApiCall { authApi.withdrawalCenter(withdrawalCenterRequest) }

    suspend fun withdrawalWorker(withdrawalWorkerRequest: WithdrawalWorkerRequest): Result<Unit> =
        safeApiCall { authApi.withdrawalWorker(withdrawalWorkerRequest) }

    suspend fun validateIdentifier(identifier: String): Result<Unit> =
        safeApiCall { authApi.validateIdentifier(identifier) }

    suspend fun validateBusinessRegistrationNumber(
        businessRegistrationNumber: String
    ): Result<BusinessRegistrationResponse> =
        safeApiCall { authApi.validateBusinessRegistrationNumber(businessRegistrationNumber) }

    suspend fun generateNewPassword(
        generateNewPasswordRequest: GenerateNewPasswordRequest
    ): Result<Unit> = safeApiCall { authApi.generateNewPassword(generateNewPasswordRequest) }

    suspend fun sendCenterVerificationRequest(): Result<Unit> =
        safeApiCall { authApi.sendCenterVerificationRequest() }

    suspend fun getDeviceToken(): String = suspendCancellableCoroutine { continuation ->
        firebaseMessaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(task.result)
            } else {
                task.exception?.let { exception ->
                    continuation.resumeWithException(exception)
                } ?: continuation.resumeWithException(
                    Exception("Unknown error occurred while fetching FCM token")
                )
            }
        }
    }
}