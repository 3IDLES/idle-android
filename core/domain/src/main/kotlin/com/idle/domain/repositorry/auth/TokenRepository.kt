package com.idle.domain.repositorry.auth

interface TokenRepository {
    suspend fun getAccessToken(): String

    suspend fun postDeviceToken(deviceToken: String, userType: String): Result<Unit>
    suspend fun deleteDeviceToken(): Result<Unit>
}