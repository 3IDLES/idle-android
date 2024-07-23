package com.idle.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInWorkerRequest(
    val phoneNumber: String,
    @SerialName("verificationNumber") val authCode: String,
)