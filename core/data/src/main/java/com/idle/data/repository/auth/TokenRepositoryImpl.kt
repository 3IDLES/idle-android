package com.idle.data.repository.auth

import com.idle.datastore.datasource.TokenDataSource
import com.idle.domain.repositorry.auth.TokenRepository
import com.idle.network.model.auth.FCMTokenRequest
import com.idle.network.source.auth.AuthDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource,
    private val authDataSource: AuthDataSource,
) : TokenRepository {
    override suspend fun getAccessToken(): String = withContext(Dispatchers.IO) {
        tokenDataSource.accessToken.first()
    }

    override suspend fun postDeviceToken(deviceToken: String): Result<Unit> =
        authDataSource.postFCMToken(FCMTokenRequest(deviceToken))

    override suspend fun updateDeviceToken(deviceToken: String): Result<Unit> =
        authDataSource.updateFCMToken(FCMTokenRequest(deviceToken))

    override suspend fun deleteDeviceToken(): Result<Unit> = authDataSource.deleteFCMToken()
}