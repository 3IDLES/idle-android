package com.tgyuu.applicant.inquiry

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.jobposting.Applicant
import com.idle.domain.model.jobposting.JobPostingSummary
import com.idle.domain.usecase.jobposting.GetApplicantsInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicantInquiryViewModel @Inject constructor(
    private val getApplicantsInfoUseCase: GetApplicantsInfoUseCase,
) : BaseViewModel() {
    private val _jobPostingSummary = MutableStateFlow<JobPostingSummary?>(null)
    val jobPostingSummary = _jobPostingSummary.asStateFlow()

    private val _applicants = MutableStateFlow<List<Applicant>>(emptyList())
    val applicants = _applicants.asStateFlow()

    suspend fun getApplicantsInfo(jobPostingId: String) = viewModelScope.launch {
        getApplicantsInfoUseCase(jobPostingId).onSuccess { (jobPostingSummary, applicants) ->
            _jobPostingSummary.value = jobPostingSummary
            _applicants.value = applicants
        }.onFailure { handleFailure(it as HttpResponseException) }
    }
}