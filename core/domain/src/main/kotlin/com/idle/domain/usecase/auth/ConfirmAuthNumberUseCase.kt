package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
import javax.inject.Inject

class ConfirmAuthNumberUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(phoneNumber: String, verificationNumber: String) =
        authRepository.confirmAuthNumber(phoneNumber, verificationNumber)
}