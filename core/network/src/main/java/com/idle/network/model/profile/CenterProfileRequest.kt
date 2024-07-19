package com.idle.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class CenterProfileRequest(
    val centerName: String,
    val officeNumber: String,
    val lotNumberAddress: String,
    val detailedAddress: String,
    val longitude: Double,
    val latitude: Double,
    val introduce: String?,
    val profileImageUrl: String?,
)
