package com.idle.network.model.config

import com.idle.domain.model.config.ForceUpdate
import kotlinx.serialization.Serializable

@Serializable
data class ForceUpdateResponse(
    val minVersion: String = "",
    val marketUrl: String = "",
    val noticeMsg: String = "",
) {
    fun toVO() = ForceUpdate(
        minVersion = minVersion,
        marketUrl = marketUrl,
        noticeMsg = noticeMsg
    )
}