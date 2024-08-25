package com.idle.domain.usecase.jobposting

import com.idle.domain.repositorry.jobposting.JobPostingRepository
import javax.inject.Inject

class GetMyFavoritesJobPostingsAppliedUseCase @Inject constructor(
    private val jobPostingRepository: JobPostingRepository,
) {
    suspend operator fun invoke(next: String?, limit: Int = 10) =
        jobPostingRepository.getMyFavoritesJobPostings(next, limit)
}