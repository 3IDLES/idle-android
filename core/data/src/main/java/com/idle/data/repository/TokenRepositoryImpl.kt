package com.idle.data.repository

import com.idle.datastore.datasource.TokenDataSource
import com.idle.domain.repositorry.TokenRepository
import com.idle.network.model.notification.DeleteFcmTokenRequest
import com.idle.network.model.notification.PostFcmTokenRequest
import com.idle.network.source.NotificationDataSource
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

    override suspend fun postDeviceToken(deviceToken: String, userType: String): Result<Unit> =
        notificationDataSource.postFCMToken(
            PostFcmTokenRequest(
                deviceToken = deviceToken,
                userType = userType,
            )
        )

    override suspend fun deleteDeviceToken(deviceToken: String): Result<Unit> =
        notificationDataSource.deleteFCMToken(DeleteFcmTokenRequest(deviceToken))
}
