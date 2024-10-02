package com.idle.domain.usecase.notification

import com.idle.domain.repositorry.auth.TokenRepository
import javax.inject.Inject

class PostDeviceTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(deviceToken: String, userType: String) =
        tokenRepository.postDeviceToken(
            deviceToken = deviceToken,
            userType = userType,
        )
}