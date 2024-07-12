package com.idle.domain.usecase.th

import com.idle.domain.repositorry.auth.AuthRepository
import javax.inject.Inject

class SignInCenterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(identifier: String, password: String) =
        authRepository.signInCenter(identifier = identifier, password = password)
}