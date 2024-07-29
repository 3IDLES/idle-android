package com.idle.network.model.profile


import kotlinx.serialization.Serializable

@Serializable
data class RegisterCenterProfileRequest(
    val centerName: String,
    val detailedAddress: String,
    val introduce: String,
    val lotNumberAddress: String,
    val officeNumber: String,
    val roadNameAddress: String
)