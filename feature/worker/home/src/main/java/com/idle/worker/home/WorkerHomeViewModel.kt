package com.idle.worker.home

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.jobposting.WorkerJobPosting
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.usecase.jobposting.AddFavoriteJobPostingUseCase
import com.idle.domain.usecase.jobposting.ApplyJobPostingUseCase
import com.idle.domain.usecase.jobposting.GetJobPostingsUseCase
import com.idle.domain.usecase.jobposting.RemoveFavoriteJobPostingUseCase
import com.idle.domain.usecase.profile.GetMyWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerHomeViewModel @Inject constructor(
    private val getMyWorkerProfileUseCase: GetMyWorkerProfileUseCase,
    private val getJobPostingsUseCase: GetJobPostingsUseCase,
    private val applyJobPostingUseCase: ApplyJobPostingUseCase,
    private val addFavoriteJobPostingUseCase: AddFavoriteJobPostingUseCase,
    private val removeFavoriteJobPostingUseCase: RemoveFavoriteJobPostingUseCase,
) : BaseViewModel() {
    private val _profile = MutableStateFlow<WorkerProfile?>(null)
    val profile = _profile.asStateFlow()

    private val next = MutableStateFlow<String?>(null)

    private val _jobPostings = MutableStateFlow<List<WorkerJobPosting>>(emptyList())
    val jobPostings = _jobPostings.asStateFlow()

    private var callType: JobPostingCallType = JobPostingCallType.IN_APP

    init {
        getJobPostings()
        viewModelScope.launch {
            getMyWorkerProfileUseCase().onSuccess {
                _profile.value = it
            }.onFailure {
                baseEvent(CareBaseEvent.ShowSnackBar(it.message.toString()))
            }
        }
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
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    private suspend fun fetchCrawlingJobPostings() {
        // Todo: 크롤링 공고 호출 로직 추가
    }

    internal fun applyJobPosting(jobPostingId: String) = viewModelScope.launch {
        applyJobPostingUseCase(
            jobPostingId = jobPostingId,
            applyMethod = ApplyMethod.APP
        ).onSuccess {

        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    internal fun addFavoriteJobPosting(jobPostingId: String) = viewModelScope.launch {
        addFavoriteJobPostingUseCase(jobPostingId).onSuccess {

        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    internal fun removeFavoriteJobPosting(jobPostingId: String) = viewModelScope.launch {
        removeFavoriteJobPostingUseCase(jobPostingId).onSuccess {

        }.onFailure { handleFailure(it as HttpResponseException) }
    }
}

enum class JobPostingCallType {
    IN_APP, CRAWLING, END
}