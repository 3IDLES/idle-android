package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
import javax.inject.Inject

class ValidateBusinessRegistrationNumberUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(businessRegistrationNumber: String) =
        authRepository.validateBusinessRegistrationNumber(businessRegistrationNumber)
}