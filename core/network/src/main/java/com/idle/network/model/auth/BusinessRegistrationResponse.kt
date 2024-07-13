package com.idle.network.model.auth

import com.idle.domain.model.auth.BusinessRegistrationInfo
import kotlinx.serialization.Serializable

@Serializable
data class BusinessRegistrationResponse(
    val businessRegistrationNumber: String = "",
    val companyName: String = "",
) {
    fun toVO(): BusinessRegistrationInfo = BusinessRegistrationInfo(
        businessRegistrationNumber = businessRegistrationNumber,
        companyName = companyName
    )
}