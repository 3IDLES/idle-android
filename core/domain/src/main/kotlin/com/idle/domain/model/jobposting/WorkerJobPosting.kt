package com.idle.domain.model.jobposting

import com.idle.domain.model.auth.Gender
import java.time.LocalDate
import java.time.LocalDateTime

data class WorkerJobPosting(
    override val id: String,
    override val distance: Int,
    override val jobPostingType: JobPostingType,
    override val isFavorite: Boolean,
    val age: Int,
    val applyDeadlineType: ApplyDeadlineType,
    val careLevel: Int,
    val gender: Gender,
    val isExperiencePreferred: Boolean,
    val lotNumberAddress: String,
    val roadNameAddress: String,
    val payAmount: Int,
    val payType: PayType,
    val startTime: String,
    val endTime: String,
    val weekdays: List<DayOfWeek>,
    val applyDeadline: LocalDate,
    val applyTime: LocalDateTime?,
) : JobPosting(id, distance, jobPostingType, isFavorite)