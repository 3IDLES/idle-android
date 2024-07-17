package com.idle.data.repository.auth

import com.idle.datastore.datasource.TokenDataSource
import com.idle.network.token.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TokenManagerImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource,
) : TokenManager {
    override fun getAccessToken(): String = runBlocking {
        tokenDataSource.accessToken.first()
    }

    override fun getRefreshToken(): String = runBlocking {
        tokenDataSource.refreshToken.first()
    }

    override suspend fun setAccessToken(accessToken: String) {
        tokenDataSource.setAccessToken(accessToken)
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        tokenDataSource.setRefreshToken(refreshToken)
    }
}