package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
import javax.inject.Inject

class ValidateIdentifierUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(identifier: String) = authRepository.validateIdentifier(identifier)
}