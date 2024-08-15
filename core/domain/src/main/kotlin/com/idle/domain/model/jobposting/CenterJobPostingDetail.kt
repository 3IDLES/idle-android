package com.idle.domain.model.jobposting

import com.idle.domain.model.job.ApplyDeadlineType
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.LifeAssistance
import com.idle.domain.model.job.MentalStatus
import com.idle.domain.model.job.PayType

data class CenterJobPostingDetail(
    val weekdays: Set<DayOfWeek>,
    val startTime: String,
    val endTime: String,
    val payType: PayType,
    val payAmount: Int,
    val id: String,
    val roadNameAddress: String,
    val lotNumberAddress: String,
    val clientName: String,
    val gender: String,
    val birthYear: Int,
    val weight: Int,
    val careLevel: Int,
    val mentalStatus: MentalStatus,
    val disease: String,
    val isMealAssistance: Boolean,
    val isBowelAssistance: Boolean,
    val isWalkingAssistance: Boolean,
    val lifeAssistance: Set<LifeAssistance>,
    val extraRequirement: String?,
    val isExperiencePreferred: Boolean,
    val applyMethod: Set<ApplyMethod>,
    val applyDeadlineType: ApplyDeadlineType,
    val applyDeadline: String,
)
