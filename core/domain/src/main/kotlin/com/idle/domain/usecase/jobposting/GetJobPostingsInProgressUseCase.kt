package com.idle.domain.usecase.jobposting

import com.idle.domain.repositorry.JobPostingRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetJobPostingsInProgressUseCase @Inject constructor(
    private val jobPostingRepository: JobPostingRepository,
) {
    suspend operator fun invoke() = coroutineScope {
        val jobPostings = jobPostingRepository.getJobPostingsInProgress()
        val deferredResults = jobPostings.map { posting ->
            async {
                val applicantCount = jobPostingRepository.getApplicantsCount(posting.id)
                posting.copy(applicantCount = applicantCount)
            }
        }

        deferredResults.map { it.await() }
    }
}
