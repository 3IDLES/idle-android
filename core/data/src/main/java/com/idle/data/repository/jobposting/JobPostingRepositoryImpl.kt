package com.idle.data.repository.jobposting

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyDeadlineType
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.LifeAssistance
import com.idle.domain.model.job.MentalStatus
import com.idle.domain.model.job.PayType
import com.idle.domain.model.jobposting.CenterJobPostingDetail
import com.idle.domain.model.jobposting.JobPosting
import com.idle.domain.repositorry.jobposting.JobPostingRepository
import com.idle.network.model.jobposting.JobPostingRequest
import com.idle.network.source.jobposting.JobPostingDataSource
import javax.inject.Inject

class JobPostingRepositoryImpl @Inject constructor(
    private val jobPostingDataSource: JobPostingDataSource
) : JobPostingRepository {
    override suspend fun postJobPosting(
        weekdays: List<DayOfWeek>,
        startTime: String,
        endTime: String,
        payType: PayType,
        payAmount: Int,
        roadNameAddress: String,
        lotNumberAddress: String,
        clientName: String,
        gender: Gender,
        birthYear: Int,
        weight: Int?,
        careLevel: Int,
        mentalStatus: MentalStatus,
        disease: String?,
        isMealAssistance: Boolean,
        isBowelAssistance: Boolean,
        isWalkingAssistance: Boolean,
        lifeAssistance: List<LifeAssistance>,
        extraRequirement: String?,
        isExperiencePreferred: Boolean,
        applyMethod: List<ApplyMethod>,
        applyDeadLineType: ApplyDeadlineType,
        applyDeadline: String?,
    ): Result<Unit> = jobPostingDataSource.postJobPosting(
        JobPostingRequest(
            weekdays = weekdays.map { it.name },
            startTime = startTime,
            endTime = endTime,
            payType = payType.name,
            payAmount = payAmount,
            roadNameAddress = roadNameAddress,
            lotNumberAddress = lotNumberAddress,
            clientName = clientName,
            gender = gender.name,
            birthYear = birthYear,
            weight = weight,
            careLevel = careLevel,
            mentalStatus = mentalStatus.name,
            disease = disease,
            isMealAssistance = isMealAssistance,
            isBowelAssistance = isBowelAssistance,
            isWalkingAssistance = isWalkingAssistance,
            lifeAssistance = lifeAssistance.map { it.name },
            extraRequirement = extraRequirement,
            isExperiencePreferred = isExperiencePreferred,
            applyMethod = applyMethod.map { it.name },
            applyDeadlineType = applyDeadLineType.name,
            applyDeadline = applyDeadline,
        )
    )

    override suspend fun updateJobPosting(
        jobPostingId: String,
        weekdays: List<DayOfWeek>,
        startTime: String,
        endTime: String,
        payType: PayType,
        payAmount: Int,
        roadNameAddress: String,
        lotNumberAddress: String,
        clientName: String,
        gender: Gender,
        birthYear: Int,
        weight: Int?,
        careLevel: Int,
        mentalStatus: MentalStatus,
        disease: String?,
        isMealAssistance: Boolean,
        isBowelAssistance: Boolean,
        isWalkingAssistance: Boolean,
        lifeAssistance: List<LifeAssistance>,
        extraRequirement: String?,
        isExperiencePreferred: Boolean,
        applyMethod: List<ApplyMethod>,
        applyDeadLineType: ApplyDeadlineType,
        applyDeadline: String?,
    ): Result<Unit> = jobPostingDataSource.updateJobPosting(
        jobPostingId = jobPostingId,
        jobPostingRequest = JobPostingRequest(
            weekdays = weekdays.map { it.name },
            startTime = startTime,
            endTime = endTime,
            payType = payType.name,
            payAmount = payAmount,
            roadNameAddress = roadNameAddress,
            lotNumberAddress = lotNumberAddress,
            clientName = clientName,
            gender = gender.name,
            birthYear = birthYear,
            weight = weight,
            careLevel = careLevel,
            mentalStatus = mentalStatus.name,
            disease = disease,
            isMealAssistance = isMealAssistance,
            isBowelAssistance = isBowelAssistance,
            isWalkingAssistance = isWalkingAssistance,
            lifeAssistance = lifeAssistance.map { it.name },
            extraRequirement = extraRequirement,
            isExperiencePreferred = isExperiencePreferred,
            applyMethod = applyMethod.map { it.name },
            applyDeadlineType = applyDeadLineType.name,
            applyDeadline = applyDeadline,
        )
    )

    override suspend fun getCenterJobPostingDetail(jobPostingId: String): Result<CenterJobPostingDetail> =
        jobPostingDataSource.getCenterJobPostingDetail(jobPostingId)
            .mapCatching { it.toVO() }

    override suspend fun getJobPostings(
        next: String?,
        limit: Int
    ): Result<Pair<String?, List<JobPosting>>> = jobPostingDataSource.getJobPostings(
        next = next,
        limit = limit
    ).mapCatching { it.toVO() }
}