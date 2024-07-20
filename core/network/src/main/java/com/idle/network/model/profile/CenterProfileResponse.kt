package com.idle.network.model.profile

import com.idle.domain.model.profile.CenterProfile
import kotlinx.serialization.Serializable

@Serializable
data class CenterProfileResponse(
    val centerName: String = "",
    val officeNumber: String = "",
    val roadNameAddress: String = "",
    val lotNumberAddress: String = "",
    val detailedAddress: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val introduce: String? = null,
    val profileImageUrl: String? = null,
) {
    fun toVO(): CenterProfile = CenterProfile(
        centerName = centerName,
        officeNumber = officeNumber,
        roadNameAddress = roadNameAddress,
        lotNumberAddress = lotNumberAddress,
        detailedAddress = detailedAddress,
        longitude = longitude,
        latitude = latitude,
        introduce = introduce ?: "",
        profileImageUrl = profileImageUrl ?: "",
    )
}
