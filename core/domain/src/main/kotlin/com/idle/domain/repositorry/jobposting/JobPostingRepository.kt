package com.idle.domain.repositorry.jobposting

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.Applicant
import com.idle.domain.model.jobposting.ApplyDeadlineType
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.CenterJobPosting
import com.idle.domain.model.jobposting.CenterJobPostingDetail
import com.idle.domain.model.jobposting.CrawlingJobPosting
import com.idle.domain.model.jobposting.CrawlingJobPostingDetail
import com.idle.domain.model.jobposting.DayOfWeek
import com.idle.domain.model.jobposting.JobPostingSummary
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.jobposting.LifeAssistance
import com.idle.domain.model.jobposting.MentalStatus
import com.idle.domain.model.jobposting.PayType
import com.idle.domain.model.jobposting.WorkerJobPosting
import com.idle.domain.model.jobposting.WorkerJobPostingDetail

interface JobPostingRepository {
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
    ): Result<Unit>

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
    ): Result<Unit>

    suspend fun getCenterJobPostingDetail(jobPostingId: String): Result<CenterJobPostingDetail>

    suspend fun getWorkerJobPostingDetail(jobPostingId: String): Result<WorkerJobPostingDetail>

    suspend fun getJobPostings(
        next: String?,
        limit: Int
    ): Result<Pair<String?, List<WorkerJobPosting>>>

    suspend fun getJobPostingsApplied(
        next: String?,
        limit: Int
    ): Result<Pair<String?, List<WorkerJobPosting>>>

    suspend fun getMyFavoritesJobPostings(): Result<List<WorkerJobPosting>>

    suspend fun getMyFavoritesCrawlingJobPostings(): Result<List<CrawlingJobPosting>>

    suspend fun getJobPostingsInProgress(): Result<List<CenterJobPosting>>

    suspend fun getJobPostingsCompleted(): Result<List<CenterJobPosting>>

    suspend fun getApplicantsCount(jobPostingId: String): Result<Int>

    suspend fun applyJobPosting(jobPostingId: String, applyMethod: ApplyMethod): Result<Unit>

    suspend fun addFavoriteJobPosting(
        jobPostingId: String,
        jobPostingType: JobPostingType,
    ): Result<Unit>

    suspend fun removeFavoriteJobPosting(jobPostingId: String): Result<Unit>

    suspend fun getApplicants(jobPostingId: String): Result<Pair<JobPostingSummary, List<Applicant>>>

    suspend fun endJobPosting(jobPostingId: String): Result<Unit>

    suspend fun deleteJobPosting(jobPostingId: String): Result<Unit>

    suspend fun getCrawlingJobPostings(
        next: String?,
        limit: Int
    ): Result<Pair<String?, List<CrawlingJobPosting>>>

    suspend fun getCrawlingJobPostingDetail(jobPostingId: String): Result<CrawlingJobPostingDetail>
}