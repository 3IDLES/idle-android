package com.idle.data

import com.idle.datastore.datasource.TokenDataSource
import com.idle.network.token.TokenProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TokenProviderImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource,
) : TokenProvider {
    override fun getAccessToken(): String = runBlocking {
        tokenDataSource.accessToken.first()
    }

    override fun getRefreshToken(): String = runBlocking {
        tokenDataSource.refreshToken.first()
    }
}