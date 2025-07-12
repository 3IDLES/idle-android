package com.idle.domain.repositorry

import com.idle.domain.model.auth.BusinessRegistrationInfo

interface AuthRepository {
    suspend fun sendPhoneNumber(phoneNumber: String)
    suspend fun confirmAuthCode(phoneNumber: String, authCode: String)
    suspend fun validateIdentifier(identifier: String)
    suspend fun validateBusinessRegistrationNumber(businessRegistrationNumber: String):
            BusinessRegistrationInfo

    suspend fun signUpWorker(
        name: String,
        birthYear: Int,
        genderType: String,
        phoneNumber: String,
        roadNameAddress: String,
        lotNumberAddress: String,
    )

    suspend fun signUpCenter(
        identifier: String,
        password: String,
        phoneNumber: String,
        managerName: String,
        businessRegistrationNumber: String,
    )

    suspend fun signInWorker(phoneNumber: String, authCode: String)
    suspend fun signInCenter(identifier: String, password: String)
    suspend fun logoutWorker()
    suspend fun logoutCenter()

    suspend fun withdrawalCenter(reason: String, password: String)
    suspend fun withdrawalWorker(reason: String)
    suspend fun generateNewPassword(newPassword: String, phoneNumber: String)
    suspend fun sendCenterVerificationRequest()
}
