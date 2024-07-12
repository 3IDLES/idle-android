package com.idle.network.model.token

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val accessToken: String?,
    val refreshToken: String?,
)