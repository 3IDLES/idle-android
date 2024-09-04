package com.idle.domain.model.jobposting

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.profile.JobSearchStatus

data class Applicant(
    val age: Int,
    val carerId: String,
    val experienceYear: Int?,
    val gender: Gender,
    val jobSearchStatus: JobSearchStatus,
    val name: String,
    val profileImageUrl: String?
)
