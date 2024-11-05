package com.idle.network.model.notification

import kotlinx.serialization.Serializable

@Serializable
data class PostFcmTokenRequest(
    val deviceToken: String,
    val userType: String,
)