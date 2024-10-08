package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.TokenRepository
import javax.inject.Inject


class GetAccessTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke() = tokenRepository.getAccessToken()
}