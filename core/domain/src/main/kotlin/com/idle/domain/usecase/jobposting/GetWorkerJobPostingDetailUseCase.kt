package com.idle.domain.usecase.jobposting

import com.idle.domain.repositorry.jobposting.JobPostingRepository
import javax.inject.Inject

class GetWorkerJobPostingDetailUseCase @Inject constructor(
    private val jobPostingRepository: JobPostingRepository,
) {
    suspend operator fun invoke(jobPostingId: String) =
        jobPostingRepository.getWorkerJobPostingDetail(jobPostingId)
}