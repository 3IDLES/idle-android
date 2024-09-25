package com.idle.network.model.notification

import kotlinx.serialization.Serializable

@Serializable
data class GetNotificationResponse(
    val notifications: List<String> = emptyList(),
)
