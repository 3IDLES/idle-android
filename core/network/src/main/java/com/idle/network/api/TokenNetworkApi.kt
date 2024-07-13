package com.idle.network.api

import com.idle.network.model.token.RefreshTokenRequest
import com.idle.network.model.token.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenNetworkApi {
    @POST("/api/v1/auth/center/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Response<TokenResponse>
}