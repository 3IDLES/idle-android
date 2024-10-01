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
    val weekdays: List<String>? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val payType: String? = null,
    val payAmount: Int? = null,
    val id: String? = null,
    val roadNameAddress: String? = null,
    val lotNumberAddress: String? = null,
    val clientName: String? = null,
    val gender: String? = null,
    val age: Int? = null,
    val weight: Int? = null,
    val careLevel: Int? = null,
    val mentalStatus: String? = null,
    val disease: String? = null,
    val isMealAssistance: Boolean? = null,
    val isBowelAssistance: Boolean? = null,
    val isWalkingAssistance: Boolean? = null,
    val lifeAssistance: List<String>? = null,
    val extraRequirement: String? = null,
    val isExperiencePreferred: Boolean? = null,
    val applyMethod: List<String>? = null,
    val applyDeadlineType: String? = null,
    val applyDeadline: String? = null,
    val jobPostingStatus: String? = null,
) {
    fun toVO() = CenterJobPostingDetail(
        weekdays = DayOfWeek.create(weekdays ?: listOf()),
        startTime = startTime ?: "",
        endTime = endTime ?: "",
        payType = PayType.create(payType ?: ""),
        payAmount = payAmount ?: 0,
        id = id ?: "",
        roadNameAddress = roadNameAddress ?: "",
        lotNumberAddress = lotNumberAddress ?: "",
        clientName = clientName ?: "케어밋",
        gender = Gender.create(gender),
        age = age ?: -1,
        weight = weight ?: -1,
        careLevel = careLevel ?: -1,
        mentalStatus = MentalStatus.create(mentalStatus),
        disease = disease,
        isMealAssistance = isMealAssistance ?: false,
        isBowelAssistance = isBowelAssistance ?: false,
        isWalkingAssistance = isWalkingAssistance ?: false,
        lifeAssistance = LifeAssistance.create(lifeAssistance ?: listOf()),
        extraRequirement = extraRequirement ?: "",
        isExperiencePreferred = isExperiencePreferred ?: false,
        applyMethod = ApplyMethod.create(applyMethod ?: listOf()),
        applyDeadlineType = ApplyDeadlineType.create(applyDeadlineType ?: ""),
        applyDeadline = applyDeadline?.let {
            LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        },
        jobPostingStatus = JobPostingStatus.create(jobPostingStatus)
    )
}
