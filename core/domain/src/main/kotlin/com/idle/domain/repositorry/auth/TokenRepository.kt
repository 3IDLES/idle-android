package com.idle.domain.repositorry.auth

interface TokenRepository {
    suspend fun getAccessToken(): String
    suspend fun putDeviceToken(token: String): Result<Unit>
}