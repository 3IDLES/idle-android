package com.idle.domain.usecase.jobposting

import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyDeadlineType
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.LifeAssistance
import com.idle.domain.model.job.MentalStatus
import com.idle.domain.model.job.PayType
import com.idle.domain.repositorry.jobposting.JobPostingRepository
import javax.inject.Inject

class UpdateJobPostingUseCase @Inject constructor(
    private val jobPostingRepository: JobPostingRepository,
) {
    suspend operator fun invoke(
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
        applyDeadlineType: ApplyDeadlineType,
        applyDeadline: String?,
    ) = jobPostingRepository.updateJobPosting(
        jobPostingId = jobPostingId,
        weekdays = weekdays,
        startTime = startTime,
        endTime = endTime,
        payType = payType,
        payAmount = payAmount,
        roadNameAddress = roadNameAddress,
        lotNumberAddress = lotNumberAddress,
        clientName = clientName,
        gender = gender,
        birthYear = birthYear,
        weight = weight,
        careLevel = careLevel,
        mentalStatus = mentalStatus,
        disease = disease,
        isMealAssistance = isMealAssistance,
        isBowelAssistance = isBowelAssistance,
        isWalkingAssistance = isWalkingAssistance,
        lifeAssistance = lifeAssistance,
        extraRequirement = extraRequirement,
        isExperiencePreferred = isExperiencePreferred,
        applyMethod = applyMethod,
        applyDeadLineType = applyDeadlineType,
        applyDeadline = applyDeadline,
    )
}