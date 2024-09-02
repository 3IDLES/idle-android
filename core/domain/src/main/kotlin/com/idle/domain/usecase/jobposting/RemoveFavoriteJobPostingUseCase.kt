package com.idle.domain.usecase.jobposting

import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.repositorry.jobposting.JobPostingRepository
import javax.inject.Inject

class RemoveFavoriteJobPostingUseCase @Inject constructor(
    private val jobPostingRepository: JobPostingRepository,
) {
    suspend operator fun invoke(
        jobPostingId: String,
        jobPostingType: JobPostingType,
    ) = jobPostingRepository.removeFavoriteJobPosting(
        jobPostingId = jobPostingId,
        jobPostingType = jobPostingType,
    )
}