package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
import javax.inject.Inject

class SendPhoneNumberUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(phoneNumber: String) = authRepository.sendPhoneNumber(phoneNumber)
}