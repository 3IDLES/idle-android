package com.idle.applicant.inquiry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.jobposting.Applicant
import com.idle.domain.model.jobposting.JobPostingSummary
import com.idle.domain.repositorry.JobPostingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicantInquiryViewModel @Inject constructor(
    private val jobPostingRepository: JobPostingRepository,
    private val errorHelper: ErrorHelper,
    val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
    private val _jobPostingSummary = MutableStateFlow<JobPostingSummary?>(null)
    val jobPostingSummary = _jobPostingSummary.asStateFlow()

    private val _applicants = MutableStateFlow<List<Applicant>>(emptyList())
    val applicants = _applicants.asStateFlow()

    suspend fun getApplicantsInfo(jobPostingId: String) = viewModelScope.launch {
        jobPostingRepository.getApplicants(jobPostingId)
            .onSuccess { (jobPostingSummary, applicants) ->
                _jobPostingSummary.value = jobPostingSummary
                _applicants.value = applicants
            }.onFailure { errorHelper.sendError(it) }
    }
}
