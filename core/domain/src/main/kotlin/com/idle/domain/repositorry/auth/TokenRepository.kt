package com.idle.domain.repositorry.auth

interface TokenRepository {
    suspend fun getAccessToken(): String
    suspend fun putFCMDeviceToken(token: String): Result<Unit>
}