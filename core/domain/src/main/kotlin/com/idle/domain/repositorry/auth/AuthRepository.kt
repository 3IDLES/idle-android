package com.idle.domain.repositorry.auth

interface AuthRepository {
    suspend fun sendPhoneNumber(phoneNumber: String): Result<Unit>

    suspend fun confirmAuthCode(phoneNumber: String, authCode: String): Result<Unit>
}