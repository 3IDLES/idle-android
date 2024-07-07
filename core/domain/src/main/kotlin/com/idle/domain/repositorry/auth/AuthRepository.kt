package com.idle.domain.repositorry.auth

interface AuthRepository {
    suspend fun sendPhoneNumber(phoneNumber: String): Result<Unit>

    suspend fun confirmAuthCode(phoneNumber: String, authCode: String): Result<Unit>

    suspend fun signUpCenter(
        identifier: String,
        password: String,
        phoneNumber: String,
        managerName: String,
        businessRegistrationNumber: String,
    ): Result<Unit>
}