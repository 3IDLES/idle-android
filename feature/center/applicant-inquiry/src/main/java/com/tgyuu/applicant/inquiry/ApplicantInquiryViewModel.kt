package com.tgyuu.applicant.inquiry

import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.profile.WorkerProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ApplicantInquiryViewModel @Inject constructor() : BaseViewModel() {
    private val _workerProfile = MutableStateFlow<WorkerProfile?>(null)
    val workerProfile = _workerProfile.asStateFlow()


}