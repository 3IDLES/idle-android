package com.idle.domain.repositorry.auth

interface TokenRepository {
    suspend fun getAccessToken(): String
    suspend fun setDeviceToken(token: String): Result<Unit>
}