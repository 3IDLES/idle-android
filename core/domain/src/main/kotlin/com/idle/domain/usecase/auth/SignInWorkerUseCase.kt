package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
import javax.inject.Inject

class SignInWorkerUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        phoneNumber: String,
        authCode: String,
    ) = authRepository.signInWorker(phoneNumber = phoneNumber, authCode = authCode)
}