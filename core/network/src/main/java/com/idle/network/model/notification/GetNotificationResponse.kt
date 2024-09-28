package com.idle.network.model.notification

import com.idle.domain.model.notification.Notification
import kotlinx.serialization.Serializable

@Serializable
data class GetNotificationResponse(
    val notifications: List<String> = emptyList(),
) {
    fun toVO() = Notification(
        something = ""
    )
}
