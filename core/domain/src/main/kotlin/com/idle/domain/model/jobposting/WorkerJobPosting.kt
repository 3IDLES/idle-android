package com.idle.domain.model.jobposting

import com.idle.domain.model.auth.Gender
import java.time.LocalDate
import java.time.LocalDateTime
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
    val applyTime: LocalDateTime?,
    val jobPostingType: JobPostingType,
    val isFavorite: Boolean,
) {
    fun calculateDeadline(): Long {
        val seoulZone = ZoneId.of("Asia/Seoul")
        val nowDate = LocalDate.now(seoulZone)
        return applyDeadline.toEpochDay() - nowDate.toEpochDay()
    }

    fun getDistanceInMinutes(): String = when (distance) {
        in 0..200 -> "5분 이내"
        in 201..400 -> "5~10분"
        in 401..700 -> "10~15분"
        in 701..1000 -> "15~20분"
        in 1001..1250 -> "20~25분"
        in 1251..1500 -> "25~30분"
        in 1501..1750 -> "30~35분"
        in 1751..2000 -> "35~40분"
        else -> "40분 이상"
    }
}