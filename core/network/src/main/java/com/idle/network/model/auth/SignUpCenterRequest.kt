package com.idle.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpCenterRequest(
    val identifier: String,
    val password: String,
    val phoneNumber: String,
    val managerName: String,
    @SerialName("centerBusinessRegistrationNumber") val businessRegistrationNumber: String,
)
