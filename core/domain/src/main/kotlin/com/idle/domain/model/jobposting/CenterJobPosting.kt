package com.idle.domain.model.jobposting

import com.idle.domain.model.auth.Gender

data class CenterJobPosting(
    val id: String,
    val lotNumberAddress: String,
    val roadNameAddress: String,
    val clientName: String,
    val gender: Gender,
    val age: Int,
    val careLevel: Int,
    val applyDeadlineType: ApplyDeadlineType,
    val applyDeadline: String,
    val createdAt: String,
    val applicantCount: Int,
)