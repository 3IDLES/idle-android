package com.idle.network.model.profile

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.profile.JobSearchStatus
import com.idle.domain.model.profile.WorkerProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetWorkerProfileResponse(
    @SerialName("carerName") val workerName: String = "",
    val age: Int = -1,
    val gender: String = "",
    val experienceYear: Int? = null,
    val phoneNumber: String = "",
    val roadNameAddress: String,
    val lotNumberAddress: String,
    val longitude: String,
    val latitude: String,
    val jobSearchStatus: String,
    val introduce: String? = null,
    val speciality: String? = null,
    val profileImageUrl: String? = null,
) {
    fun toVo() = WorkerProfile(
        workerName = workerName,
        age = age,
        gender = Gender.create(gender),
        experienceYear = experienceYear,
        phoneNumber = phoneNumber,
        roadNameAddress = roadNameAddress,
        lotNumberAddress = lotNumberAddress,
        longitude = longitude,
        latitude = latitude,
        jobSearchStatus = JobSearchStatus.create(jobSearchStatus),
    )
}