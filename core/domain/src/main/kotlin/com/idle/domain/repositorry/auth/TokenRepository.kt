package com.idle.domain.repositorry.auth

import com.idle.domain.model.notification.Notification

interface TokenRepository {
    suspend fun getAccessToken(): String

    suspend fun postDeviceToken(deviceToken: String): Result<Unit>
    suspend fun updateDeviceToken(deviceToken: String): Result<Unit>
    suspend fun deleteDeviceToken(): Result<Unit>
}