package com.idle.domain.model.jobposting

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyDeadlineType
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.PayType
import java.time.LocalDate
import java.time.ZoneId

data class WorkerJobPosting(
    val id: String,
    val age: Int,
    val applyDeadline: LocalDate,
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
    val applyTime: LocalDate,
    val isFavorite: Boolean,
) {
    fun calculateDeadline(): Long {
        val seoulZone = ZoneId.of("Asia/Seoul")
        val nowDate = LocalDate.now(seoulZone)
        return applyDeadline.toEpochDay() - nowDate.toEpochDay()
    }
}