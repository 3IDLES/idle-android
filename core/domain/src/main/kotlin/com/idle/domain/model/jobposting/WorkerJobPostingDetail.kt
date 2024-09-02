package com.idle.domain.model.jobposting

import com.idle.domain.model.auth.Gender
import java.time.LocalDate
import java.time.LocalDateTime

data class WorkerJobPostingDetail(
    override val id: String,
    override val distance: Int,
    override val applyDeadline: LocalDate,
    override val jobPostingType: JobPostingType,
    override val isFavorite: Boolean,
    override val longitude: String,
    override val latitude: String,
    val weekdays: Set<DayOfWeek>,
    val startTime: String,
    val endTime: String,
    val payType: PayType,
    val payAmount: Int,
    val roadNameAddress: String,
    val lotNumberAddress: String,
    val gender: Gender,
    val age: Int,
    val weight: Int?,
    val careLevel: Int,
    val mentalStatus: MentalStatus,
    val disease: String?,
    val isMealAssistance: Boolean,
    val isBowelAssistance: Boolean,
    val isWalkingAssistance: Boolean,
    val lifeAssistance: Set<LifeAssistance>,
    val extraRequirement: String?,
    val isExperiencePreferred: Boolean,
    val applyMethod: Set<ApplyMethod>,
    val applyDeadlineType: ApplyDeadlineType,
    val centerId: String,
    val centerName: String,
    val centerRoadNameAddress: String,
    val applyTime: LocalDateTime?,
) : JobPostingDetail(id, distance, applyDeadline, jobPostingType, isFavorite, latitude, longitude)