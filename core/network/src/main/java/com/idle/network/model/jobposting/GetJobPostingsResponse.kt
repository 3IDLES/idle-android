package com.idle.network.model.jobposting

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyDeadlineType
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.PayType
import com.idle.domain.model.jobposting.JobPosting
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetJobPostingsResponse(
    @SerialName("items") val jobPostingResponses: List<JobPostingResponse>,
    val next: String?,
    val total: Int
) {
    fun toVO(): Pair<String?, List<JobPosting>> =
        next to jobPostingResponses.map { it.toVO() }
}

@Serializable
data class JobPostingResponse(
    val age: Int,
    val applyDeadline: String,
    val applyDeadlineType: String,
    val careLevel: Int,
    val distance: Int,
    val endTime: String,
    val gender: String,
    val id: String,
    val isExperiencePreferred: Boolean,
    val lotNumberAddress: String,
    val payAmount: Int,
    val payType: String,
    val roadNameAddress: String,
    val startTime: String,
    val weekdays: List<String>
) {
    fun toVO(): JobPosting = JobPosting(
        age = age,
        applyDeadline = applyDeadline,
        applyDeadlineType = ApplyDeadlineType.create(applyDeadlineType),
        careLevel = careLevel,
        distance = distance,
        endTime = endTime,
        gender = Gender.create(gender),
        id = id,
        isExperiencePreferred = isExperiencePreferred,
        lotNumberAddress = lotNumberAddress,
        payAmount = payAmount,
        payType = PayType.create(payType),
        roadNameAddress = roadNameAddress,
        startTime = startTime,
        weekdays = DayOfWeek.create(weekdays)
            .toList()
            .sortedBy { it.ordinal },
    )
}