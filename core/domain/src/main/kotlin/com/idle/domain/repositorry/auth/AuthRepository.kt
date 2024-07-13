package com.idle.domain.repositorry.auth

import com.idle.domain.model.auth.BusinessRegistrationInfo

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

    suspend fun signInCenter(
        identifier: String,
        password: String,
    ): Result<Unit>

    suspend fun validateIdentifier(identifier: String): Result<Unit>

    suspend fun validateBusinessRegistrationNumber(businessRegistrationNumber: String):
            Result<BusinessRegistrationInfo>
}