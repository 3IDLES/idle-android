package com.idle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class SignInCenterRequest(
    val identifier: String,
    val password: String,
)
