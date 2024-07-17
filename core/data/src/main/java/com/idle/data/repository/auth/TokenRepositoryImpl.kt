package com.idle.data.repository.auth

import com.idle.datastore.datasource.TokenDataSource
import com.idle.domain.repositorry.auth.TokenRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource,
) : TokenRepository {
    override suspend fun getAccessToken(): String = tokenDataSource.accessToken.first()
}