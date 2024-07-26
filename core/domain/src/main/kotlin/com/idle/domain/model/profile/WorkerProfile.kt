package com.idle.domain.model.profile

import com.idle.domain.model.auth.Gender

data class WorkerProfile(
    val workerName: String = "",
    val age: Int = -1,
    val gender: Gender = Gender.NONE,
    val experienceYear: Int? = null,
    val phoneNumber: String = "",
    val roadNameAddress: String = "",
    val lotNumberAddress: String = "",
    val longitude: String = "",
    val latitude: String = "",
    val jobSearchStatus: JobSearchStatus = JobSearchStatus.UNKNOWN,
    val introduce: String? = null,
    val speciality: String? = null,
    val profileImageUrl: String? = null,
)
