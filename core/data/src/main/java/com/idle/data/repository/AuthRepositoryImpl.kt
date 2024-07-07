package com.idle.data.repository

import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.network.source.AuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
) : AuthRepository {
    override suspend fun sendAuthNumber(phoneNumber: String): Result<Unit> =
        authDataSource.sendAuthNumber(phoneNumber)
}