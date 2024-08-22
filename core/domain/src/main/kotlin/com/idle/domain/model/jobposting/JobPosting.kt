package com.idle.domain.model.jobposting

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyDeadlineType
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.PayType

data class JobPosting(
    val id: String,
    val age: Int,
    val applyDeadline: String,
    val applyDeadlineType: ApplyDeadlineType,
    val careLevel: Int,
    val distance: Int,
    val gender: Gender,
    val isExperiencePreferred: Boolean,
    val lotNumberAddress: String,
    val roadNameAddress: String,
    val payAmount: Int,
    val payType: PayType,
    val startTime: String,
    val endTime: String,
    val weekdays: List<DayOfWeek>,
)