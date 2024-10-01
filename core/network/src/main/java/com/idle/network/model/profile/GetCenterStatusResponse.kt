package com.idle.network.model.profile

import com.idle.domain.model.profile.CenterManagerAccountStatus
import com.idle.domain.model.profile.CenterRegistrationStatus
import kotlinx.serialization.Serializable

@Serializable
data class GetCenterStatusResponse(
    val id: String? = null,
    val managerName: String? = null,
    val phoneNumber: String? = null,
    val centerManagerAccountStatus: String? = null,
) {
    fun toVO() = CenterRegistrationStatus(
        id = id ?: "",
        managerName = managerName ?: "",
        phoneNumber = phoneNumber ?: "",
        centerManagerAccountStatus = CenterManagerAccountStatus.create(
            centerManagerAccountStatus ?: ""
        )
    )
}
