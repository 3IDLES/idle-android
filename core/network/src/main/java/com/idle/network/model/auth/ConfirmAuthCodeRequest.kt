package com.idle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmAuthCodeRequest(
    val phoneNumber: String,
    val authCode: String,
)
