package com.idle.network.model.jobposting

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.ApplyDeadlineType
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.CenterJobPostingDetail
import com.idle.domain.model.jobposting.DayOfWeek
import com.idle.domain.model.jobposting.JobPostingStatus
import com.idle.domain.model.jobposting.LifeAssistance
import com.idle.domain.model.jobposting.MentalStatus
import com.idle.domain.model.jobposting.PayType
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    val age: Int = 0,
    val weight: Int? = null,
    val careLevel: Int = 0,
    val mentalStatus: String = "",
    val disease: String? = null,
    val isMealAssistance: Boolean = false,
    val isBowelAssistance: Boolean = false,
    val isWalkingAssistance: Boolean = false,
    val lifeAssistance: List<String>? = null,
    val extraRequirement: String? = null,
    val isExperiencePreferred: Boolean = false,
    val applyMethod: List<String> = listOf(),
    val applyDeadlineType: String = "",
    val applyDeadline: String? = null,
    val jobPostingStatus: String = "",
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
        gender = Gender.create(gender),
        age = age,
        weight = weight,
        careLevel = careLevel,
        mentalStatus = MentalStatus.create(mentalStatus),
        disease = disease,
        isMealAssistance = isMealAssistance,
        isBowelAssistance = isBowelAssistance,
        isWalkingAssistance = isWalkingAssistance,
        lifeAssistance = LifeAssistance.create(lifeAssistance ?: listOf()),
        extraRequirement = extraRequirement,
        isExperiencePreferred = isExperiencePreferred,
        applyMethod = ApplyMethod.create(applyMethod),
        applyDeadlineType = ApplyDeadlineType.create(applyDeadlineType),
        applyDeadline = applyDeadline?.let {
            LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        },
        jobPostingStatus = JobPostingStatus.create(jobPostingStatus),
    )
}