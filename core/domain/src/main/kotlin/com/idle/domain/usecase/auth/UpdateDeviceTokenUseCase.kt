package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.TokenRepository
import javax.inject.Inject

class UpdateDeviceTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(deviceToken: String) = tokenRepository.updateDeviceToken(deviceToken)
}