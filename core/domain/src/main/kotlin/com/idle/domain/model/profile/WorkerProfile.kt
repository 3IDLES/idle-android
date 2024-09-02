package com.idle.domain.model.profile

import com.idle.domain.model.auth.Gender

data class WorkerProfile(
    val workerName: String,
    val age: Int,
    val gender: Gender,
    val experienceYear: Int?,
    val phoneNumber: String,
    val roadNameAddress: String,
    val lotNumberAddress: String,
    val longitude: String,
    val latitude: String,
    val jobSearchStatus: JobSearchStatus,
    val introduce: String?,
    val speciality: String?,
    val profileImageUrl: String?,
)