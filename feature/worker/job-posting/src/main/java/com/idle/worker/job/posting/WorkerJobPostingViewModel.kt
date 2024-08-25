package com.idle.worker.job.posting

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.jobposting.WorkerJobPosting
import com.idle.domain.usecase.jobposting.GetJobPostingsAppliedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerJobPostingViewModel @Inject constructor(
    private val getJobPostingsAppliedUseCase: GetJobPostingsAppliedUseCase,
) : BaseViewModel() {
    private val _recruitmentPostStatus = MutableStateFlow(RecruitmentPostStatus.APPLY)
    val recruitmentPostStatus = _recruitmentPostStatus.asStateFlow()

    private val next = MutableStateFlow<String?>(null)

    private val _appliedJobPostings = MutableStateFlow<List<WorkerJobPosting>>(emptyList())
    val appliedJobPostings = _appliedJobPostings.asStateFlow()

    internal fun setRecruitmentPostStatus(recruitmentPostStatus: RecruitmentPostStatus) {
        _recruitmentPostStatus.value = recruitmentPostStatus
    }

    private var appliedJobPostingCallType: JobPostingCallType = JobPostingCallType.IN_APP
    private var favoriteJobPostingCallType: JobPostingCallType = JobPostingCallType.IN_APP

    internal fun getAppliedJobPostings() = viewModelScope.launch {
        getJobPostingsAppliedUseCase(next = next.value).onSuccess { (nextId, postings) ->
            next.value = nextId

            if (nextId == null) {
                appliedJobPostingCallType = JobPostingCallType.END
            }

            _appliedJobPostings.value += postings
        }.onFailure {
            baseEvent(CareBaseEvent.Error(it.message.toString()))
        }
    }
}

enum class RecruitmentPostStatus(val displayName: String) {
    APPLY("지원한 공고"),
    MARKED("찜한 공고")
}

enum class JobPostingCallType {
    IN_APP, CRAWLING, END
}