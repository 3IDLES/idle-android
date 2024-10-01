package com.idle.network.model.profile

import com.idle.domain.model.profile.CenterProfile
import kotlinx.serialization.Serializable

@Serializable
data class GetCenterProfileResponse(
    val centerName: String? = null,
    val officeNumber: String? = null,
    val roadNameAddress: String? = null,
    val lotNumberAddress: String? = null,
    val detailedAddress: String? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val introduce: String? = null,
    val profileImageUrl: String? = null,
) {
    fun toVO(): CenterProfile = CenterProfile(
        centerName = centerName ?: "",
        officeNumber = officeNumber ?: "",
        roadNameAddress = roadNameAddress ?: "",
        lotNumberAddress = lotNumberAddress ?: "",
        detailedAddress = detailedAddress ?: "",
        longitude = longitude ?: 0.0,
        latitude = latitude ?: 0.0,
        introduce = introduce,
        profileImageUrl = profileImageUrl,
    )
}
