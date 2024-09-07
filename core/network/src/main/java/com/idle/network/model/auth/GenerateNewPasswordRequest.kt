package com.idle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class GenerateNewPasswordRequest(
    val phoneNumber: String,
    val newPassword: String,
)