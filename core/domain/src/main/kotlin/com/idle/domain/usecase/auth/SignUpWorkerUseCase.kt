package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.domain.util.formatPhoneNumber
import javax.inject.Inject

class SignUpWorkerUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        name: String,
        birthYear: Int,
        genderType: String,
        phoneNumber: String,
        roadNameAddress: String,
        lotNumberAddress: String,
    ): Result<Unit> {
        val formattedPhoneNumber = formatPhoneNumber(phoneNumber)

        return authRepository.signUpWorker(
            name = name,
            birthYear = birthYear,
            genderType = genderType,
            phoneNumber = formattedPhoneNumber,
            roadNameAddress = roadNameAddress,
            lotNumberAddress = lotNumberAddress,
        )
    }
}