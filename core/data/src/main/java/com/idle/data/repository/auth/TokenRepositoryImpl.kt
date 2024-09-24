package com.idle.data.repository.auth

import com.idle.datastore.datasource.TokenDataSource
import com.idle.domain.repositorry.auth.TokenRepository
import com.idle.network.model.auth.PostDeviceTokenRequest
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

    override suspend fun setDeviceToken(token: String): Result<Unit> =
        authDataSource.postDeviceToken(PostDeviceTokenRequest(token))
}