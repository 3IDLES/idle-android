package com.idle.data.repository.auth

import com.idle.datastore.datasource.TokenDataSource
import com.idle.network.di.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokenManagerImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource,
) : TokenManager {
    override suspend fun getAccessToken(): String = withContext(Dispatchers.IO) {
        tokenDataSource.accessToken.first()
    }

    override suspend fun getRefreshToken(): String = withContext(Dispatchers.IO) {
        tokenDataSource.refreshToken.first()
    }

    override suspend fun setAccessToken(accessToken: String) {
        tokenDataSource.setAccessToken(accessToken)
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        tokenDataSource.setRefreshToken(refreshToken)
    }
}