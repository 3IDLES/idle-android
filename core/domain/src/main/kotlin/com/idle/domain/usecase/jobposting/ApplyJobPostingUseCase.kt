package com.idle.domain.usecase.jobposting

import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.repositorry.jobposting.JobPostingRepository
import javax.inject.Inject

class ApplyJobPostingUseCase @Inject constructor(
    private val jobPostingRepository: JobPostingRepository,
) {
    suspend operator fun invoke(jobPostingId: String, applyMethod: ApplyMethod) =
        jobPostingRepository.applyJobPosting(jobPostingId = jobPostingId, applyMethod = applyMethod)
}