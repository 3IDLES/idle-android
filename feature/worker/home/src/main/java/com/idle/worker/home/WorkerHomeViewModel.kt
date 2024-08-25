package com.idle.worker.home

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.jobposting.WorkerJobPosting
import com.idle.domain.usecase.jobposting.ApplyJobPostingUseCase
import com.idle.domain.usecase.jobposting.GetJobPostingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerHomeViewModel @Inject constructor(
    private val getJobPostingsUseCase: GetJobPostingsUseCase,
    private val applyJobPostingUseCase: ApplyJobPostingUseCase,
) : BaseViewModel() {
    private val next = MutableStateFlow<String?>(null)

    private val _jobPostings = MutableStateFlow<List<WorkerJobPosting>>(emptyList())
    val jobPostings = _jobPostings.asStateFlow()

    private var callType: JobPostingCallType = JobPostingCallType.IN_APP

    init {
        getJobPostings()
    }

    fun getJobPostings() = viewModelScope.launch {
        if (callType == JobPostingCallType.END) return@launch

        when (callType) {
            JobPostingCallType.IN_APP -> fetchInAppJobPostings()
            JobPostingCallType.CRAWLING -> fetchCrawlingJobPostings()
            JobPostingCallType.END -> return@launch
        }
    }

    private suspend fun fetchInAppJobPostings() {
        getJobPostingsUseCase(next = next.value).onSuccess { (nextId, postings) ->
            next.value = nextId
            if (nextId == null) {
                callType = JobPostingCallType.CRAWLING
            }
            _jobPostings.value += postings
        }.onFailure {

        }
    }

    private suspend fun fetchCrawlingJobPostings() {
        // Todo: 크롤링 공고 호출 로직 추가
    }

    fun applyJobPosting(jobPostingId: String) = viewModelScope.launch {
        applyJobPostingUseCase(
            jobPostingId = jobPostingId,
            applyMethod = ApplyMethod.APP
        ).onSuccess {

        }.onFailure {
            baseEvent(CareBaseEvent.Error(it.message.toString()))
        }
    }
}

enum class JobPostingCallType {
    IN_APP, CRAWLING, END
}