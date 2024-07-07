package com.idle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmRequest(
    val phoneNumber: String,
    val verificationNumber: String,
)
