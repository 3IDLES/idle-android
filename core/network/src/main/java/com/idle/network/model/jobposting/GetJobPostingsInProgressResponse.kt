package com.idle.network.model.jobposting


import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyDeadlineType
import com.idle.domain.model.jobposting.CenterJobPosting
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetJobPostingsInProgressResponse(
    @SerialName("jobPostings")
    val jobPostings: List<CenterJobPostingResponse>
) {
    fun toVO(): List<CenterJobPosting> = jobPostings.map { it.toVO() }
}

@Serializable
data class CenterJobPostingResponse(
    val id: String,
    val roadNameAddress: String,
    val lotNumberAddress: String,
    val clientName: String,
    val gender: String,
    val age: Int,
    val careLevel: Int,
    val applyDeadlineType: String,
    val applyDeadline: String,
    val createdAt: String,
) {
    fun toVO(): CenterJobPosting = CenterJobPosting(
        age = age,
        applyDeadline = applyDeadline,
        applyDeadlineType = ApplyDeadlineType.create(applyDeadlineType),
        careLevel = careLevel,
        gender = Gender.create(gender),
        id = id,
        lotNumberAddress = lotNumberAddress,
        roadNameAddress = roadNameAddress,
        clientName = clientName,
        createdAt = createdAt,
    )
}