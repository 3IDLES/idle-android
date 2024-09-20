package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.domain.util.formatBusinessRegistrationNumber
import com.idle.domain.util.formatPhoneNumber
import javax.inject.Inject

class SignUpCenterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        identifier: String,
        password: String,
        phoneNumber: String,
        managerName: String,
        businessRegistrationNumber: String,
    ): Result<Unit> {
        val formattedPhoneNumber = formatPhoneNumber(phoneNumber)
        val formattedNumber = formatBusinessRegistrationNumber(businessRegistrationNumber)

        return authRepository.signUpCenter(
            identifier = identifier,
            password = password,
            phoneNumber = formattedPhoneNumber,
            managerName = managerName,
            businessRegistrationNumber = formattedNumber,
        )
    }
}