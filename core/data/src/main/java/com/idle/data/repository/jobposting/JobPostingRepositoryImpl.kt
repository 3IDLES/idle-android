package com.idle.data.repository.jobposting

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.ApplyDeadlineType
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.DayOfWeek
import com.idle.domain.model.jobposting.LifeAssistance
import com.idle.domain.model.jobposting.MentalStatus
import com.idle.domain.model.jobposting.PayType
import com.idle.domain.model.jobposting.Applicant
import com.idle.domain.model.jobposting.CenterJobPosting
import com.idle.domain.model.jobposting.CenterJobPostingDetail
import com.idle.domain.model.jobposting.JobPostingSummary
import com.idle.domain.model.jobposting.WorkerJobPosting
import com.idle.domain.model.jobposting.WorkerJobPostingDetail
import com.idle.domain.repositorry.jobposting.JobPostingRepository
import com.idle.network.model.jobposting.ApplyJobPostingRequest
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

    override suspend fun getWorkerJobPostingDetail(jobPostingId: String): Result<WorkerJobPostingDetail> =
        jobPostingDataSource.getWorkerJobPostingDetail(jobPostingId)
            .mapCatching { it.toVO() }

    override suspend fun getJobPostings(
        next: String?,
        limit: Int
    ): Result<Pair<String?, List<WorkerJobPosting>>> = jobPostingDataSource.getJobPostings(
        next = next,
        limit = limit
    ).mapCatching { it.toVO() }

    override suspend fun getJobPostingsApplied(
        next: String?,
        limit: Int
    ): Result<Pair<String?, List<WorkerJobPosting>>> = jobPostingDataSource.getJobPostingsApplied(
        next = next,
        limit = limit
    ).mapCatching { it.toVO() }

    override suspend fun getMyFavoritesJobPostings(
        next: String?,
        limit: Int
    ): Result<Pair<String?, List<WorkerJobPosting>>> =
        jobPostingDataSource.getMyFavoritesJobPostings(
            next = next,
            limit = limit
        ).mapCatching { it.toVO() }

    override suspend fun getJobPostingsInProgress(): Result<List<CenterJobPosting>> =
        jobPostingDataSource.getJobPostingsInProgress().mapCatching { it.toVO() }

    override suspend fun getJobPostingsCompleted(): Result<List<CenterJobPosting>> =
        jobPostingDataSource.getJobPostingsCompleted().mapCatching { it.toVO() }

    override suspend fun getApplicantsCount(jobPostingId: String): Result<Int> =
        jobPostingDataSource.getApplicantCount(jobPostingId).mapCatching { it.applicantCount }

    override suspend fun applyJobPosting(
        jobPostingId: String,
        applyMethod: ApplyMethod,
    ): Result<Unit> =
        jobPostingDataSource.applyJobPosting(
            ApplyJobPostingRequest(jobPostingId = jobPostingId, applyMethodType = applyMethod.name)
        )

    override suspend fun addFavoriteJobPosting(jobPostingId: String): Result<Unit> =
        jobPostingDataSource.addFavoriteJobPosting(jobPostingId)

    override suspend fun removeFavoriteJobPosting(jobPostingId: String): Result<Unit> =
        jobPostingDataSource.removeFavoriteJobPosting(jobPostingId)

    override suspend fun getApplicants(jobPostingId: String): Result<Pair<JobPostingSummary, List<Applicant>>> =
        jobPostingDataSource.getApplicants(jobPostingId).mapCatching { it.toVO() }

    override suspend fun endJobPosting(jobPostingId: String): Result<Unit> =
        jobPostingDataSource.endJobPosting(jobPostingId)

    override suspend fun deleteJobPosting(jobPostingId: String): Result<Unit> =
        jobPostingDataSource.deleteJobPosting(jobPostingId)
}