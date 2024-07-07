package com.idle.data.repository

import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.network.model.auth.SendPhoneRequest
import com.idle.network.model.auth.ConfirmAuthCodeRequest
import com.idle.network.source.AuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
) : AuthRepository {
    override suspend fun sendPhoneNumber(phoneNumber: String): Result<Unit> =
        authDataSource.sendPhoneNumber(SendPhoneRequest(phoneNumber))

    override suspend fun confirmAuthCode(
        phoneNumber: String,
        authCode: String,
    ): Result<Unit> =
        authDataSource.confirmAuthCode(ConfirmAuthCodeRequest(phoneNumber, authCode))
}