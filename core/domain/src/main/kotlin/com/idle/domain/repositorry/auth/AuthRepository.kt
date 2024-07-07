package com.idle.domain.repositorry.auth

interface AuthRepository {
    suspend fun sendAuthNumber(phoneNumber: String): Result<Unit>
}