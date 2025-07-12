package com.idle.data.repository

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.Applicant
import com.idle.domain.model.jobposting.ApplyDeadlineType
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.CenterJobPosting
import com.idle.domain.model.jobposting.CenterJobPostingDetail
import com.idle.domain.model.jobposting.CrawlingJobPosting
import com.idle.domain.model.jobposting.CrawlingJobPostingDetail
import com.idle.domain.model.jobposting.CrawlingJobPostingPage
import com.idle.domain.model.jobposting.DayOfWeek
import com.idle.domain.model.jobposting.JobPostingSummary
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.jobposting.LifeAssistance
import com.idle.domain.model.jobposting.MentalStatus
import com.idle.domain.model.jobposting.PayType
import com.idle.domain.model.jobposting.SharedJobPostingInfo
import com.idle.domain.model.jobposting.WorkerJobPosting
import com.idle.domain.model.jobposting.WorkerJobPostingDetail
import com.idle.domain.model.jobposting.WorkerJobPostingPage
import com.idle.domain.repositorry.JobPostingRepository
import com.idle.network.model.jobposting.ApplyJobPostingRequest
import com.idle.network.model.jobposting.FavoriteJobPostingRequest
import com.idle.network.model.jobposting.JobPostingRequest
import com.idle.network.source.JobPostingDataSource
import javax.inject.Inject

class JobPostingRepositoryImpl @Inject constructor(
    private val jobPostingDataSource: JobPostingDataSource
) : JobPostingRepository {
    override var sharedJobPostingInfo: SharedJobPostingInfo? = null
        get() {
            val currentValue = field
            field = null
            return currentValue
        }

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
    ) = jobPostingDataSource.postJobPosting(
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
        applyMethod: List<ApplyMethod>?,
        applyDeadLineType: ApplyDeadlineType,
        applyDeadline: String?,
    ) = jobPostingDataSource.updateJobPosting(
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
            applyMethod = applyMethod?.map { it.name },
            applyDeadlineType = applyDeadLineType.name,
            applyDeadline = applyDeadline,
        )
    )

    override suspend fun getCenterJobPostingDetail(jobPostingId: String): CenterJobPostingDetail =
        jobPostingDataSource.getCenterJobPostingDetail(jobPostingId).toVO()

    override suspend fun getWorkerJobPostingDetail(jobPostingId: String): WorkerJobPostingDetail =
        jobPostingDataSource.getWorkerJobPostingDetail(jobPostingId).toVO()

    override suspend fun getJobPostings(
        next: String?,
        limit: Int
    ): WorkerJobPostingPage = jobPostingDataSource.getJobPostings(
        next = next,
        limit = limit
    ).toVO()

    override suspend fun getJobPostingsApplied(
        next: String?,
        limit: Int
    ): WorkerJobPostingPage = jobPostingDataSource.getJobPostingsApplied(
        next = next,
        limit = limit
    ).toVO()

    override suspend fun getMyFavoritesJobPostings(): List<WorkerJobPosting> =
        jobPostingDataSource.getMyFavoriteJobPostings().toVO()

    override suspend fun getMyFavoritesCrawlingJobPostings(): List<CrawlingJobPosting> =
        jobPostingDataSource.getMyFavoriteCrawlingJobPostings().toVO()

    override suspend fun getJobPostingsInProgress(): List<CenterJobPosting> =
        jobPostingDataSource.getJobPostingsInProgress().toVO()

    override suspend fun getJobPostingsCompleted(): List<CenterJobPosting> =
        jobPostingDataSource.getJobPostingsCompleted().toVO()

    override suspend fun getApplicantsCount(jobPostingId: String) =
        jobPostingDataSource.getApplicantCount(jobPostingId).applicantCount

    override suspend fun applyJobPosting(
        jobPostingId: String,
        applyMethod: ApplyMethod,
    ) = jobPostingDataSource.applyJobPosting(
        ApplyJobPostingRequest(jobPostingId = jobPostingId, applyMethodType = applyMethod.name)
    )

    override suspend fun addFavoriteJobPosting(
        jobPostingId: String,
        jobPostingType: JobPostingType,
    ) = jobPostingDataSource.addFavoriteJobPosting(
        jobPostingId = jobPostingId,
        favoriteJobPostingRequest = FavoriteJobPostingRequest(jobPostingType.name),
    )

    override suspend fun removeFavoriteJobPosting(jobPostingId: String) =
        jobPostingDataSource.removeFavoriteJobPosting(jobPostingId = jobPostingId)

    override suspend fun getApplicants(jobPostingId: String): Pair<JobPostingSummary, List<Applicant>> =
        jobPostingDataSource.getApplicants(jobPostingId).toVO()

    override suspend fun endJobPosting(jobPostingId: String) =
        jobPostingDataSource.endJobPosting(jobPostingId)

    override suspend fun deleteJobPosting(jobPostingId: String) =
        jobPostingDataSource.deleteJobPosting(jobPostingId)

    override suspend fun getCrawlingJobPostings(
        next: String?,
        limit: Int,
        distance: Int,
    ): CrawlingJobPostingPage =
        jobPostingDataSource.getCrawlingJobPostings(next, limit, distance).toVO()

    override suspend fun getCrawlingJobPostingDetail(jobPostingId: String): CrawlingJobPostingDetail =
        jobPostingDataSource.getCrawlingJobPostingsDetail(jobPostingId).toVO()
}
