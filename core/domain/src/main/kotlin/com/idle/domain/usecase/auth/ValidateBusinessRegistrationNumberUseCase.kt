package com.idle.domain.usecase.auth

import com.idle.domain.model.auth.BusinessRegistrationInfo
import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.domain.util.formatBusinessRegistrationNumber
import javax.inject.Inject

class ValidateBusinessRegistrationNumberUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(businessRegistrationNumber: String): Result<BusinessRegistrationInfo> =
        runCatching {
            val formattedNumber = formatBusinessRegistrationNumber(businessRegistrationNumber)

            return authRepository.validateBusinessRegistrationNumber(formattedNumber)
        }
}