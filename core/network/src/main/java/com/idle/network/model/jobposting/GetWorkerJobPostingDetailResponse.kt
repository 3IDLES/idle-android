package com.idle.network.model.jobposting


import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.ApplyDeadlineType
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.DayOfWeek
import com.idle.domain.model.jobposting.JobPostingType
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
    val age: Int? = null,
    val applyDeadline: String? = null,
    val applyDeadlineType: String? = null,
    val applyMethod: List<String>? = null,
    val careLevel: Int? = null,
    val centerId: String? = null,
    val centerName: String? = null,
    val centerRoadNameAddress: String? = null,
    val disease: String? = null,
    val distance: Int? = null,
    val endTime: String? = null,
    val extraRequirement: String? = null,
    val gender: String? = null,
    val id: String? = null,
    val isBowelAssistance: Boolean? = null,
    val isExperiencePreferred: Boolean? = null,
    val isMealAssistance: Boolean? = null,
    val isWalkingAssistance: Boolean? = null,
    val latitude: String? = null,
    val lifeAssistance: List<String>? = null,
    val longitude: String? = null,
    val lotNumberAddress: String? = null,
    val mentalStatus: String? = null,
    val payAmount: Int? = null,
    val payType: String? = null,
    val roadNameAddress: String? = null,
    val startTime: String? = null,
    val weekdays: List<String>? = null,
    val weight: Int? = null,
    val applyTime: String? = null,
    val isFavorite: Boolean? = null,
    val jobPostingType: String? = null,
    val centerOfficeNumber: String? = null,
) {
    fun toVO() = WorkerJobPostingDetail(
        weekdays = DayOfWeek.create(weekdays ?: listOf()),
        startTime = startTime ?: "",
        endTime = endTime ?: "",
        payType = PayType.create(payType ?: ""),
        payAmount = payAmount ?: 0,
        id = id ?: "",
        roadNameAddress = roadNameAddress ?: "",
        lotNumberAddress = lotNumberAddress ?: "",
        gender = Gender.create(gender ?: ""),
        age = age ?: -1,
        weight = weight ?: -1,
        careLevel = careLevel ?: -1,
        mentalStatus = MentalStatus.create(mentalStatus ?: ""),
        disease = disease,
        isMealAssistance = isMealAssistance ?: false,
        isBowelAssistance = isBowelAssistance ?: false,
        isWalkingAssistance = isWalkingAssistance ?: false,
        lifeAssistance = LifeAssistance.create(lifeAssistance ?: listOf()),
        extraRequirement = extraRequirement,
        isExperiencePreferred = isExperiencePreferred ?: false,
        applyMethod = ApplyMethod.create(applyMethod ?: listOf()),
        applyDeadlineType = ApplyDeadlineType.create(applyDeadlineType ?: ""),
        applyDeadline = applyDeadline?.let {
            LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } ?: LocalDate.now(),
        distance = distance ?: 0,
        centerName = centerName ?: "",
        centerRoadNameAddress = centerRoadNameAddress ?: "",
        centerId = centerId ?: "",
        longitude = longitude ?: "0.0",
        latitude = latitude ?: "0.0",
        applyTime = applyTime?.let {
            LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        },
        isFavorite = isFavorite ?: false,
        jobPostingType = JobPostingType.create(jobPostingType ?: ""),
        centerOfficeNumber = centerOfficeNumber ?: ""
    )
}