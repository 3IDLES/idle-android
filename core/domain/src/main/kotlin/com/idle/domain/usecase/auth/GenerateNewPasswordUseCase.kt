package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
import javax.inject.Inject

class GenerateNewPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(newPassword: String, phoneNumber: String) =
        authRepository.generateNewPassword(newPassword = newPassword, phoneNumber = phoneNumber)
}