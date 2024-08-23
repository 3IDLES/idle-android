package com.idle.center.home

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.jobposting.CenterJobPosting
import com.idle.domain.model.jobposting.WorkerJobPosting
import com.idle.domain.usecase.jobposting.GetJobPostingsInProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterHomeViewModel @Inject constructor(
    private val getJobPostingsInProgressUseCase: GetJobPostingsInProgressUseCase,
) : BaseViewModel() {
    private val _recruitmentPostStatus = MutableStateFlow(RecruitmentPostStatus.ONGOING)
    val recruitmentPostStatus = _recruitmentPostStatus.asStateFlow()

    private val _Worker_jobPostingsInProgress = MutableStateFlow<List<CenterJobPosting>>(emptyList())
    val jobPostingsInProgress = _Worker_jobPostingsInProgress.asStateFlow()

    internal fun setRecruitmentPostStatus(recruitmentPostStatus: RecruitmentPostStatus) {
        _recruitmentPostStatus.value = recruitmentPostStatus
    }

    internal fun getJobPostingsInProgress() = viewModelScope.launch {
        getJobPostingsInProgressUseCase().onSuccess {
            _Worker_jobPostingsInProgress.value = it
        }.onFailure { baseEvent(CareBaseEvent.Error(it.toString())) }
    }
}

enum class RecruitmentPostStatus(val displayName: String) {
    ONGOING("진행 중인 공고"),
    PREVIOUS("이전 공고")
}