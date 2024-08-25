package com.idle.domain.repositorry.jobposting

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyDeadlineType
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.LifeAssistance
import com.idle.domain.model.job.MentalStatus
import com.idle.domain.model.job.PayType
import com.idle.domain.model.jobposting.CenterJobPosting
import com.idle.domain.model.jobposting.CenterJobPostingDetail
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
        applyMethod: List<ApplyMethod>,
        applyDeadLineType: ApplyDeadlineType,
        applyDeadline: String?,
    ): Result<Unit>

    suspend fun getCenterJobPostingDetail(jobPostingId: String): Result<CenterJobPostingDetail>

    suspend fun getWorkerJobPostingDetail(jobPostingId: String): Result<WorkerJobPostingDetail>

    suspend fun getJobPostings(
        next: String?,
        limit: Int
    ): Result<Pair<String?, List<WorkerJobPosting>>>

    suspend fun getJobPostingsInProgress(): Result<List<CenterJobPosting>>

    suspend fun applyJobPosting(jobPostingId: String, applyMethod: ApplyMethod): Result<Unit>

    suspend fun addFavoriteJobPosting(jobPostingId: String): Result<Unit>

    suspend fun removeFavoriteJobPosting(jobPostingId: String): Result<Unit>
}