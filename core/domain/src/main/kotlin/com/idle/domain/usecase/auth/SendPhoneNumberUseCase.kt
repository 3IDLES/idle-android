package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.domain.util.formatPhoneNumber
import javax.inject.Inject

class SendPhoneNumberUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(phoneNumber: String): Result<Unit> = runCatching {
        val formattedPhoneNumber = formatPhoneNumber(phoneNumber)
        authRepository.sendPhoneNumber(formattedPhoneNumber)
    }
}