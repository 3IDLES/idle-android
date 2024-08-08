package com.idle.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpWorkerRequest(
    @SerialName("carerName") val name: String,
    val birthYear: Int,
    val genderType: String,
    val phoneNumber: String,
    val roadNameAddress: String,
    val lotNumberAddress: String,
)