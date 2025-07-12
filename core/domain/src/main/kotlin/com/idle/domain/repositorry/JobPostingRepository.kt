package com.idle.domain.repositorry

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

interface JobPostingRepository {
    var sharedJobPostingInfo: SharedJobPostingInfo?

    suspend fun postJobPosting(
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
    )

    suspend fun updateJobPosting(
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
    )

    suspend fun getCenterJobPostingDetail(jobPostingId: String): CenterJobPostingDetail

    suspend fun getWorkerJobPostingDetail(jobPostingId: String): WorkerJobPostingDetail

    suspend fun getJobPostings(
        next: String?,
        limit: Int = 10,
    ): WorkerJobPostingPage

    suspend fun getJobPostingsApplied(
        next: String?,
        limit: Int = 10,
    ): WorkerJobPostingPage

    suspend fun getMyFavoritesJobPostings(): List<WorkerJobPosting>

    suspend fun getMyFavoritesCrawlingJobPostings(): List<CrawlingJobPosting>

    suspend fun getJobPostingsInProgress(): List<CenterJobPosting>

    suspend fun getJobPostingsCompleted(): List<CenterJobPosting>

    suspend fun getApplicantsCount(jobPostingId: String): Int

    suspend fun applyJobPosting(jobPostingId: String, applyMethod: ApplyMethod)

    suspend fun addFavoriteJobPosting(
        jobPostingId: String,
        jobPostingType: JobPostingType,
    )

    suspend fun removeFavoriteJobPosting(jobPostingId: String)

    suspend fun getApplicants(jobPostingId: String): Pair<JobPostingSummary, List<Applicant>>

    suspend fun endJobPosting(jobPostingId: String)
    suspend fun deleteJobPosting(jobPostingId: String)

    suspend fun getCrawlingJobPostings(
        next: String?,
        limit: Int = 10,
        distance: Int = 15,
    ): CrawlingJobPostingPage

    suspend fun getCrawlingJobPostingDetail(jobPostingId: String): CrawlingJobPostingDetail
}
