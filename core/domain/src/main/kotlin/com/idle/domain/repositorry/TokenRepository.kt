package com.idle.domain.repositorry

interface TokenRepository {
    suspend fun getAccessToken(): String
    suspend fun postDeviceToken(deviceToken: String, userType: String): Result<Unit>
    suspend fun deleteDeviceToken(deviceToken: String): Result<Unit>
}
