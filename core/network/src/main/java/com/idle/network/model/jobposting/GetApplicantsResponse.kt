package com.idle.network.model.jobposting


import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.ApplyDeadlineType
import com.idle.domain.model.jobposting.Applicant
import com.idle.domain.model.jobposting.JobPostingSummary
import com.idle.domain.model.profile.JobSearchStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetApplicantsResponse(
    @SerialName("jobPostingApplicants")
    val jobPostingApplicantResponses: List<JobPostingApplicantResponse>,
    @SerialName("jobPostingSummaryDto")
    val jobPostingSummaryResponse: JobPostingSummaryResponse
) {
    fun toVO() = jobPostingSummaryResponse.toVO() to jobPostingApplicantResponses.map {
        it.toVO()
    }
}

@Serializable
data class JobPostingSummaryResponse(
    val age: Int,
    val applyDeadline: String,
    val applyDeadlineType: String,
    val careLevel: Int,
    val clientName: String,
    val createdAt: String,
    val gender: String,
    val id: String,
    val lotNumberAddress: String,
    val roadNameAddress: String
) {
    fun toVO() = JobPostingSummary(
        age = age,
        applyDeadline = applyDeadline,
        applyDeadlineType = ApplyDeadlineType.create(applyDeadlineType),
        careLevel = careLevel,
        clientName = clientName,
        createdAt = createdAt,
        gender = Gender.create(gender),
        id = id,
        lotNumberAddress = lotNumberAddress,
        roadNameAddress = roadNameAddress,
    )
}

@Serializable
data class JobPostingApplicantResponse(
    val age: Int,
    val carerId: String,
    val experienceYear: Int,
    val gender: String,
    val jobSearchStatus: String,
    val name: String,
    val profileImageUrl: String
) {
    fun toVO() = Applicant(
        age = age,
        carerId = carerId,
        experienceYear = experienceYear,
        gender = Gender.create(gender),
        jobSearchStatus = JobSearchStatus.create(jobSearchStatus),
        name = name,
        profileImageUrl = profileImageUrl,
    )
}