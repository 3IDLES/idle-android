package com.idle.network.model.profile

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.profile.JobSearchStatus
import com.idle.domain.model.profile.WorkerProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetWorkerProfileResponse(
    @SerialName("carerName") val workerName: String? = null,
    val age: Int? = null,
    val gender: String? = null,
    val experienceYear: Int? = null,
    val phoneNumber: String? = null,
    val roadNameAddress: String? = null,
    val lotNumberAddress: String? = null,
    val longitude: String? = null,
    val latitude: String? = null,
    val jobSearchStatus: String? = null,
    val introduce: String? = null,
    val speciality: String? = null,
    val profileImageUrl: String? = null,
) {
    fun toVo() = WorkerProfile(
        workerName = workerName ?: "",
        age = age ?: -1,
        gender = Gender.create(gender ?: ""),
        experienceYear = experienceYear,
        phoneNumber = phoneNumber ?: "",
        roadNameAddress = roadNameAddress ?: "",
        lotNumberAddress = lotNumberAddress ?: "",
        longitude = longitude ?: "0.0",
        latitude = latitude ?: "0.0",
        jobSearchStatus = JobSearchStatus.create(jobSearchStatus),
        introduce = introduce,
        speciality = speciality,
        profileImageUrl = profileImageUrl
    )
}
