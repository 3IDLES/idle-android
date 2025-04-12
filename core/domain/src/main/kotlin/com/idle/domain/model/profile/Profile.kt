package com.idle.domain.model.profile

import com.idle.domain.model.auth.Gender

sealed class Profile

data class WorkerProfile(
    val workerId: String,
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
) : Profile()

data class CenterProfile(
    val centerId: String,
    val centerName: String,
    val officeNumber: String,
    val roadNameAddress: String,
    val lotNumberAddress: String,
    val detailedAddress: String,
    val longitude: Double,
    val latitude: Double,
    val introduce: String?,
    val profileImageUrl: String?,
) : Profile()
