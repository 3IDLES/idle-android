package com.idle.network.model.notification

import com.idle.domain.model.notification.Notification
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMyNotificationResponse(
    @SerialName("items") val notifications: List<@Contextual Notification>,
    val next: String? = null,
    val total: Int? = null,
) {
    fun toVO(): Pair<String?, List<Notification>> = next to notifications
}