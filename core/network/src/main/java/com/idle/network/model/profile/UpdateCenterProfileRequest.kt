package com.idle.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCenterProfileRequest(
    val officeNumber: String,
    val introduce: String?,
)
