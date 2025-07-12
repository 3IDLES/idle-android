package com.idle.network.source

import com.google.firebase.messaging.FirebaseMessaging
import com.idle.network.api.AuthApi
import com.idle.network.model.auth.BusinessRegistrationResponse
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
import com.idle.network.util.onResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class AuthDataSource @Inject constructor(
    private val authApi: AuthApi,
    private val firebaseMessaging: FirebaseMessaging,
) {
    suspend fun sendPhoneNumber(sendPhoneRequest: SendPhoneRequest): Unit =
        authApi.sendPhoneNumber(sendPhoneRequest).onResponse()

    suspend fun confirmAuthCode(confirmAuthCodeRequest: ConfirmAuthCodeRequest): Unit =
        authApi.confirmAuthCode(confirmAuthCodeRequest).onResponse()

    suspend fun signUpCenter(signUpCenterRequest: SignUpCenterRequest): Unit =
        authApi.signUpCenter(signUpCenterRequest).onResponse()

    suspend fun signInCenter(signInCenterRequest: SignInCenterRequest): TokenResponse =
        authApi.signInCenter(signInCenterRequest).onResponse()

    suspend fun signUpWorker(signUpWorkerRequest: SignUpWorkerRequest): TokenResponse =
        authApi.signUpWorker(signUpWorkerRequest).onResponse()

    suspend fun signInWorker(signInWorkerRequest: SignInWorkerRequest): TokenResponse =
        authApi.signInWorker(signInWorkerRequest).onResponse()

    suspend fun logoutWorker(): Unit = authApi.logoutWorker().onResponse()

    suspend fun logoutCenter(): Unit = authApi.logoutCenter().onResponse()

    suspend fun withdrawalCenter(withdrawalCenterRequest: WithdrawalCenterRequest): Unit =
        authApi.withdrawalCenter(withdrawalCenterRequest).onResponse()

    suspend fun withdrawalWorker(withdrawalWorkerRequest: WithdrawalWorkerRequest): Unit =
        authApi.withdrawalWorker(withdrawalWorkerRequest).onResponse()

    suspend fun validateIdentifier(identifier: String): Unit =
        authApi.validateIdentifier(identifier).onResponse()

    suspend fun validateBusinessRegistrationNumber(
        businessRegistrationNumber: String
    ): BusinessRegistrationResponse =
        authApi.validateBusinessRegistrationNumber(businessRegistrationNumber).onResponse()

    suspend fun generateNewPassword(
        generateNewPasswordRequest: GenerateNewPasswordRequest
    ): Unit = authApi.generateNewPassword(generateNewPasswordRequest).onResponse()

    suspend fun sendCenterVerificationRequest(): Unit =
        authApi.sendCenterVerificationRequest().onResponse()

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
