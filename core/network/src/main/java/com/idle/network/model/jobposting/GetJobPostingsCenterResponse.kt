package com.idle.network.model.jobposting


import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.ApplyDeadlineType
import com.idle.domain.model.jobposting.CenterJobPosting
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetJobPostingsCenterResponse(
    @SerialName("jobPostings") val jobPostings: List<CenterJobPostingResponse>? = null,
) {
    fun toVO(): List<CenterJobPosting> = jobPostings?.map { it.toVO() } ?: emptyList()
}

@Serializable
data class CenterJobPostingResponse(
    val id: String? = null,
    val roadNameAddress: String? = null,
    val lotNumberAddress: String? = null,
    val clientName: String? = null,
    val gender: String? = null,
    val age: Int? = null,
    val careLevel: Int? = null,
    val applyDeadlineType: String? = null,
    val applyDeadline: String? = null,
    val createdAt: String? = null,
) {
    fun toVO(): CenterJobPosting = CenterJobPosting(
        age = age ?: -1,
        applyDeadline = applyDeadline ?: "",
        applyDeadlineType = ApplyDeadlineType.create(applyDeadlineType ?: ""),
        careLevel = careLevel ?: -1,
        gender = Gender.create(gender ?: ""),
        id = id ?: "",
        lotNumberAddress = lotNumberAddress ?: "",
        roadNameAddress = roadNameAddress ?: "",
        clientName = clientName ?: "",
        createdAt = createdAt ?: "",
        applicantCount = 0,
    )
}