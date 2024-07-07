package com.idle.data.repository

import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.network.model.auth.AuthRequest
import com.idle.network.model.auth.ConfirmRequest
import com.idle.network.source.AuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
) : AuthRepository {
    override suspend fun sendAuthNumber(phoneNumber: String): Result<Unit> =
        authDataSource.sendAuthNumber(AuthRequest(phoneNumber))

    override suspend fun confirmAuthNumber(
        phoneNumber: String,
        verificationNumber: String,
    ): Result<Unit> =
        authDataSource.confirmAuthNumber(ConfirmRequest(phoneNumber, verificationNumber))
}