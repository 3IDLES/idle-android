package com.idle.domain.usecase.jobposting

import com.idle.domain.repositorry.jobposting.JobPostingRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetJobPostingsInProgressUseCase @Inject constructor(
    private val jobPostingRepository: JobPostingRepository,
) {
    suspend operator fun invoke() = coroutineScope {
        val jobPosting = jobPostingRepository.getJobPostingsInProgress()
        jobPosting.mapCatching { jobPostings ->
            val deferredResults = jobPostings.map { jobPosting ->
                async {
                    val applicantCount = jobPostingRepository.getApplicantsCount(jobPosting.id).getOrThrow()
                    jobPosting.copy(applicantCount = applicantCount)
                }
            }

            deferredResults.map { it.await() }
        }
    }
}