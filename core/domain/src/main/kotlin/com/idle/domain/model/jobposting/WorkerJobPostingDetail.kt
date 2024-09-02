package com.idle.domain.model.jobposting

import com.idle.domain.model.auth.Gender
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

data class WorkerJobPostingDetail(
    val weekdays: Set<DayOfWeek>,
    val startTime: String,
    val endTime: String,
    val longitude: String,
    val latitude: String,
    val payType: PayType,
    val payAmount: Int,
    val id: String,
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
    val applyDeadline: LocalDate,
    val centerId: String,
    val centerName: String,
    val centerRoadNameAddress: String,
    val distance: Int,
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
