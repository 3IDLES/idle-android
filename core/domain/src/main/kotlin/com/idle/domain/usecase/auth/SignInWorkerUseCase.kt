package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.domain.util.formatPhoneNumber
import javax.inject.Inject

class SignInWorkerUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        phoneNumber: String,
        authCode: String,
    ): Result<Unit> {
        val formattedPhoneNumber = formatPhoneNumber(phoneNumber)

        return authRepository.signInWorker(phoneNumber = formattedPhoneNumber, authCode = authCode)
    }
}