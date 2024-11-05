package com.idle.network.model.notification

import kotlinx.serialization.Serializable

@Serializable
data class DeleteFcmTokenRequest(
    val deviceToken: String,
)
