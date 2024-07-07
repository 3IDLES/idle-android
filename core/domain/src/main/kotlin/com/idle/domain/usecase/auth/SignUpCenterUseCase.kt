package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
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
    ) = authRepository.signUpCenter(
        identifier = identifier,
        password = password,
        phoneNumber = phoneNumber,
        managerName = managerName,
        businessRegistrationNumber = businessRegistrationNumber,
    )
}