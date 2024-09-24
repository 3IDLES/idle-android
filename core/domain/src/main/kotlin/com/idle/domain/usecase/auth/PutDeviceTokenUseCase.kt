package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.TokenRepository
import javax.inject.Inject

class PutDeviceTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(token: String) = tokenRepository.putDeviceToken(token)
}