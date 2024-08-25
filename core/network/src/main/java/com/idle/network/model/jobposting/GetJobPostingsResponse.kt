package com.idle.network.model.jobposting

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyDeadlineType
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.PayType
import com.idle.domain.model.jobposting.WorkerJobPosting
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class GetJobPostingsResponse(
    @SerialName("items") val workerJobPostingRespons: List<WorkerJobPostingResponse>,
    val next: String?,
    val total: Int
) {
    fun toVO(): Pair<String?, List<WorkerJobPosting>> =
        next to workerJobPostingRespons.map { it.toVO() }
}

@Serializable
data class WorkerJobPostingResponse(
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
    fun toVO(): WorkerJobPosting = WorkerJobPosting(
        age = age,
        applyDeadline = LocalDate.parse(applyDeadline, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
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