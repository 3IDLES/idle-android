package com.idle.center.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.jobposting.CenterJobPosting
import com.idle.domain.usecase.jobposting.GetJobPostingsCompletedUseCase
import com.idle.domain.usecase.jobposting.GetJobPostingsInProgressUseCase
import com.idle.domain.usecase.profile.GetMyCenterProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterHomeViewModel @Inject constructor(
    private val getJobPostingsInProgressUseCase: GetJobPostingsInProgressUseCase,
    private val getJobPostingsCompletedUseCase: GetJobPostingsCompletedUseCase,
) : BaseViewModel() {
    private val _recruitmentPostStatus = MutableStateFlow(RecruitmentPostStatus.IN_PROGRESS)
    val recruitmentPostStatus = _recruitmentPostStatus.asStateFlow()

    private val _jobPostingsInProgress = MutableStateFlow<List<CenterJobPosting>>(emptyList())
    val jobPostingsInProgress = _jobPostingsInProgress.asStateFlow()

    private val _jobPostingsCompleted = MutableStateFlow<List<CenterJobPosting>>(emptyList())
    val jobPostingsCompleted = _jobPostingsCompleted.asStateFlow()

    internal fun setRecruitmentPostStatus(recruitmentPostStatus: RecruitmentPostStatus) {
        _recruitmentPostStatus.value = recruitmentPostStatus
    }

    internal fun clearJobPostingStatus() {
        _jobPostingsInProgress.value = emptyList()
        _jobPostingsCompleted.value = emptyList()
    }

    internal fun getJobPostingsInProgress() = viewModelScope.launch {
        getJobPostingsInProgressUseCase().onSuccess {
            _jobPostingsInProgress.value = it
        }.onFailure {
            handleFailure(it as HttpResponseException)
        }
    }

    internal fun getJobPostingsCompleted() = viewModelScope.launch {
        getJobPostingsCompletedUseCase().onSuccess {
            _jobPostingsCompleted.value = it
        }.onFailure { handleFailure(it as HttpResponseException) }
    }
}

enum class RecruitmentPostStatus(val displayName: String) {
    IN_PROGRESS("진행 중인 공고"),
    COMPLETED("이전 공고")
}