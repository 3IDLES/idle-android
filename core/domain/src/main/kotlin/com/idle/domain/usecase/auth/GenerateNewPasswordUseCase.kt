package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.domain.util.formatPhoneNumber
import javax.inject.Inject

class GenerateNewPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(newPassword: String, phoneNumber: String): Result<Unit> {
        val formattedPhoneNumber = formatPhoneNumber(phoneNumber)

        return authRepository.generateNewPassword(
            newPassword = newPassword,
            phoneNumber = formattedPhoneNumber
        )
    }
}