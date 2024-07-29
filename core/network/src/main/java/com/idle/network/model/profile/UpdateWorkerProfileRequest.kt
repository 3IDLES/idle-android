package com.idle.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateWorkerProfileRequest(
    val experienceYear: Int?,
    val roadNameAddress: String,
    val lotNumberAddress: String,
    val jobSearchStatus: String,
    val introduce: String?,
    val speciality: String,
)
