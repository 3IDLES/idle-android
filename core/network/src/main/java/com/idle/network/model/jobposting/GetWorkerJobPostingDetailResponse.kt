package com.idle.network.model.jobposting


import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.ApplyDeadlineType
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.DayOfWeek
import com.idle.domain.model.jobposting.LifeAssistance
import com.idle.domain.model.jobposting.MentalStatus
import com.idle.domain.model.jobposting.PayType
import com.idle.domain.model.jobposting.WorkerJobPostingDetail
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class GetWorkerJobPostingDetailResponse(
    val age: Int,
    val applyDeadline: String,
    val applyDeadlineType: String,
    val applyMethod: List<String>,
    val careLevel: Int,
    val centerId: String,
    val centerName: String,
    val centerRoadNameAddress: String,
    val disease: String?,
    val distance: Int,
    val endTime: String,
    val extraRequirement: String?,
    val gender: String,
    val id: String,
    val isBowelAssistance: Boolean,
    val isExperiencePreferred: Boolean,
    val isMealAssistance: Boolean,
    val isWalkingAssistance: Boolean,
    val latitude: String,
    val lifeAssistance: List<String>,
    val longitude: String,
    val lotNumberAddress: String,
    val mentalStatus: String,
    val payAmount: Int,
    val payType: String,
    val roadNameAddress: String,
    val startTime: String,
    val weekdays: List<String>,
    val weight: Int?,
    val applyTime: String?,
    val isFavorite: Boolean,
) {
    fun toVO() = WorkerJobPostingDetail(
        weekdays = DayOfWeek.create(weekdays),
        startTime = startTime,
        endTime = endTime,
        payType = PayType.create(payType),
        payAmount = payAmount,
        id = id,
        roadNameAddress = roadNameAddress,
        lotNumberAddress = lotNumberAddress,
        gender = Gender.create(gender),
        age = age,
        weight = weight,
        careLevel = careLevel,
        mentalStatus = MentalStatus.create(mentalStatus),
        disease = disease,
        isMealAssistance = isMealAssistance,
        isBowelAssistance = isBowelAssistance,
        isWalkingAssistance = isWalkingAssistance,
        lifeAssistance = LifeAssistance.create(lifeAssistance),
        extraRequirement = extraRequirement,
        isExperiencePreferred = isExperiencePreferred,
        applyMethod = ApplyMethod.create(applyMethod),
        applyDeadlineType = ApplyDeadlineType.create(applyDeadlineType),
        applyDeadline = LocalDate.parse(applyDeadline, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        distance = distance,
        centerName = centerName,
        centerRoadNameAddress = centerRoadNameAddress,
        centerId = centerId,
        longitude = longitude,
        latitude = latitude,
        applyTime = applyTime?.let{
            LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        },
        isFavorite = isFavorite,
    )
}