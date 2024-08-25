package com.idle.domain.model.jobposting

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyDeadlineType

data class JobPostingSummary(
    val id: String,
    val age: Int,
    val applyDeadline: String,
    val applyDeadlineType: ApplyDeadlineType,
    val careLevel: Int,
    val clientName: String,
    val createdAt: String,
    val gender: Gender,
    val lotNumberAddress: String,
    val roadNameAddress: String
)
