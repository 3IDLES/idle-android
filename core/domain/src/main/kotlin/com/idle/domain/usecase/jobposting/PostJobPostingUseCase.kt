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

class PostJobPostingUseCase @Inject constructor(
    private val jobPostingRepository: JobPostingRepository,
) {
    suspend operator fun invoke(
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
        lifeAssistance: List<LifeAssistance>?,
        speciality: String?,
        isExperiencePreferred: Boolean,
        applyMethod: List<ApplyMethod>,
        applyDeadLineType: ApplyDeadlineType,
        applyDeadline: String,
    ) = jobPostingRepository.postJobPosting(
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
        speciality = speciality,
        isExperiencePreferred = isExperiencePreferred,
        applyMethod = applyMethod,
        applyDeadLineType = applyDeadLineType,
        applyDeadline = applyDeadline,
    )
}