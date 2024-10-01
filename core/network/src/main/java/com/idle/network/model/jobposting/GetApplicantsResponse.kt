package com.idle.network.model.jobposting


import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.Applicant
import com.idle.domain.model.jobposting.ApplyDeadlineType
import com.idle.domain.model.jobposting.JobPostingSummary
import com.idle.domain.model.profile.JobSearchStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetApplicantsResponse(
    @SerialName("jobPostingApplicants")
    val jobPostingApplicantResponses: List<JobPostingApplicantResponse>? = null,
    @SerialName("jobPostingSummaryDto")
    val jobPostingSummaryResponse: JobPostingSummaryResponse? = null,
) {
    fun toVO() = (jobPostingSummaryResponse?.toVO() ?: JobPostingSummary(
        age = -1,
        applyDeadline = "",
        applyDeadlineType = ApplyDeadlineType.create(""),
        careLevel = -1,
        clientName = "케어밋",
        createdAt = "",
        gender = Gender.create(""),
        id = "-1",
        lotNumberAddress = "",
        roadNameAddress = ""
    )) to (jobPostingApplicantResponses?.map { it.toVO() } ?: emptyList())
}

@Serializable
data class JobPostingSummaryResponse(
    val age: Int? = null,
    val applyDeadline: String? = null,
    val applyDeadlineType: String? = null,
    val careLevel: Int? = null,
    val clientName: String? = null,
    val createdAt: String? = null,
    val gender: String? = null,
    val id: String? = null,
    val lotNumberAddress: String? = null,
    val roadNameAddress: String? = null,
) {
    fun toVO() = JobPostingSummary(
        age = age ?: -1,
        applyDeadline = applyDeadline ?: "",
        applyDeadlineType = ApplyDeadlineType.create(applyDeadlineType ?: ""),
        careLevel = careLevel ?: -1,
        clientName = clientName ?: "",
        createdAt = createdAt ?: "",
        gender = Gender.create(gender ?: ""),
        id = id ?: "",
        lotNumberAddress = lotNumberAddress ?: "",
        roadNameAddress = roadNameAddress ?: "",
    )
}

@Serializable
data class JobPostingApplicantResponse(
    val age: Int? = null,
    val carerId: String? = null,
    val experienceYear: Int? = null,
    val gender: String? = null,
    val jobSearchStatus: String? = null,
    val name: String? = null,
    val profileImageUrl: String? = null,
) {
    fun toVO() = Applicant(
        age = age ?: -1,
        carerId = carerId ?: "",
        experienceYear = experienceYear,
        gender = Gender.create(gender ?: ""),
        jobSearchStatus = JobSearchStatus.create(jobSearchStatus ?: ""),
        name = name ?: "",
        profileImageUrl = profileImageUrl
    )
}