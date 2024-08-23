package com.idle.worker.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.jobposting.JobPosting
import com.idle.domain.usecase.jobposting.GetJobPostingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerHomeViewModel @Inject constructor(
    private val getJobPostingsUseCase: GetJobPostingsUseCase
) : BaseViewModel() {
    private val next = MutableStateFlow<String?>(null)

    private val _jobPostings = MutableStateFlow<List<JobPosting>>(emptyList())
    val jobPostings = _jobPostings.asStateFlow()

    private var callType: JobPostingCallType = JobPostingCallType.IN_APP

    init {
        getJobPostings()
    }

    fun getJobPostings() = viewModelScope.launch {
        if (callType == JobPostingCallType.END) return@launch

        Log.d("test", callType.toString())

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
}

enum class JobPostingCallType {
    IN_APP, CRAWLING, END
}