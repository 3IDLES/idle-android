package com.idle.network.model.jobposting

import com.idle.domain.model.job.ApplyDeadlineType
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.LifeAssistance
import com.idle.domain.model.job.MentalStatus
import com.idle.domain.model.job.PayType
import com.idle.domain.model.jobposting.CenterJobPostingDetail
import kotlinx.serialization.Serializable

@Serializable
data class GetCenterJobPostingDetailResponse(
    val weekdays: List<String> = listOf(),
    val startTime: String = "",
    val endTime: String = "",
    val payType: String = "",
    val payAmount: Int = 0,
    val id: String = "",
    val roadNameAddress: String = "",
    val lotNumberAddress: String = "",
    val clientName: String = "",
    val gender: String = "",
    val birthYear: Int = 0,
    val weight: Int? = null,
    val careLevel: Int = 0,
    val mentalStatus: String = "",
    val disease: String? = null,
    val isMealAssistance: Boolean = false,
    val isBowelAssistance: Boolean = false,
    val isWalkingAssistance: Boolean = false,
    val lifeAssistance: List<String> = listOf(),
    val extraRequirement: String? = null,
    val isExperiencePreferred: Boolean = false,
    val applyMethod: List<String> = listOf(),
    val applyDeadlineType: String = "",
    val applyDeadline: String = "",
) {
    fun toVO() = CenterJobPostingDetail(
        weekdays = DayOfWeek.create(weekdays),
        startTime = startTime,
        endTime = endTime,
        payType = PayType.create(payType),
        payAmount = payAmount,
        id = id,
        roadNameAddress = roadNameAddress,
        lotNumberAddress = lotNumberAddress,
        clientName = clientName,
        gender = gender,
        birthYear = birthYear,
        weight = weight ?: -1,
        careLevel = careLevel,
        mentalStatus = MentalStatus.create(mentalStatus),
        disease = disease ?: "",
        isMealAssistance = isMealAssistance,
        isBowelAssistance = isBowelAssistance,
        isWalkingAssistance = isWalkingAssistance,
        lifeAssistance = LifeAssistance.create(lifeAssistance),
        extraRequirement = extraRequirement,
        isExperiencePreferred = isExperiencePreferred,
        applyMethod = ApplyMethod.create(applyMethod),
        applyDeadlineType = ApplyDeadlineType.create(applyDeadlineType),
        applyDeadline = applyDeadline,
    )
}