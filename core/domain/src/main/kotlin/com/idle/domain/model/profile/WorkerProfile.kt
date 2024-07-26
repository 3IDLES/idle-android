package com.idle.domain.model.profile

import com.idle.domain.model.auth.Gender

data class WorkerProfile(
    val workerName: String = "",
    val age: String = "",
    val gender: Gender = Gender.NONE,
    val experienceYear: String = "",
    val phoneNumber: String = "",
    val roadNameAddress: String = "",
    val lotNumberAddress: String = "",
    val longitude: String = "",
    val latitude: String = "",
    val jobSearchStatus: String = "",
    val introduce: String? = null,
    val speciality: String? = null,
    val profileImageUrl: String? = null,
)
