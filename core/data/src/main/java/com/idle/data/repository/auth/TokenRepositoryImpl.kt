package com.idle.data.repository.auth

import com.idle.datastore.datasource.TokenDataSource
import com.idle.domain.repositorry.auth.TokenRepository
import com.idle.network.model.auth.FCMTokenRequest
import com.idle.network.source.notification.NotificationDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource,
    private val notificationDataSource: NotificationDataSource,
) : TokenRepository {
    override suspend fun getAccessToken(): String = withContext(Dispatchers.IO) {
        tokenDataSource.accessToken.first()
    }

    override suspend fun postDeviceToken(deviceToken: String): Result<Unit> =
        notificationDataSource.postFCMToken(FCMTokenRequest(deviceToken))

    override suspend fun updateDeviceToken(deviceToken: String): Result<Unit> =
        notificationDataSource.updateFCMToken(FCMTokenRequest(deviceToken))

    override suspend fun deleteDeviceToken(): Result<Unit> = notificationDataSource.deleteFCMToken()
}