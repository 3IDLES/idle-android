package com.idle.network.model.notification

import kotlinx.serialization.Serializable

@Serializable
data class GetUnreadNotificationCountResponse(
    val unreadNotificationCount: Int? = null,
)
