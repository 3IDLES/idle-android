package com.idle.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class CenterProfileRequest(
    val officeNumber: String,
    val introduce: String?,
)
