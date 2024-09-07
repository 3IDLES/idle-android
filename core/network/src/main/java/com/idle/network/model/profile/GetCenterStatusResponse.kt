package com.idle.network.model.profile

import com.idle.domain.model.profile.CenterManagerAccountStatus
import com.idle.domain.model.profile.CenterRegistrationStatus
import kotlinx.serialization.Serializable

@Serializable
data class GetCenterStatusResponse(
    val id: String,
    val managerName: String,
    val phoneNumber: String,
    val centerManagerAccountStatus: String,
) {
    fun toVO() = CenterRegistrationStatus(
        id = id,
        managerName = managerName,
        phoneNumber = phoneNumber,
        centerManagerAccountStatus = CenterManagerAccountStatus.create(centerManagerAccountStatus),
    )
}
