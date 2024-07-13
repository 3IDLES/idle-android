package com.idle.data

import com.idle.datastore.datasource.TokenDataSource
import com.idle.network.token.TokenProvider
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TokenProviderImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource,
) : TokenProvider {
    override suspend fun getAccessToken(): String = tokenDataSource.accessToken.first()

    override suspend fun getRefreshToken(): String = tokenDataSource.refreshToken.first()
}