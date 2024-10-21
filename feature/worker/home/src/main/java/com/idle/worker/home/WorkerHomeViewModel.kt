package com.idle.worker.home

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.EventHandler
import com.idle.binding.base.MainEvent
import com.idle.binding.base.SnackBarType
import com.idle.domain.model.error.ErrorHandler
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.CrawlingJobPosting
import com.idle.domain.model.jobposting.JobPosting
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.jobposting.WorkerJobPosting
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.usecase.config.ShowNotificationCenterUseCase
import com.idle.domain.usecase.jobposting.AddFavoriteJobPostingUseCase
import com.idle.domain.usecase.jobposting.ApplyJobPostingUseCase
import com.idle.domain.usecase.jobposting.GetCrawlingJobPostingsUseCase
import com.idle.domain.usecase.jobposting.GetJobPostingsUseCase
import com.idle.domain.usecase.jobposting.RemoveFavoriteJobPostingUseCase
import com.idle.domain.usecase.notification.GetUnreadNotificationCountUseCase
import com.idle.domain.usecase.profile.GetLocalMyWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorkerHomeViewModel @Inject constructor(
    private val getLocalMyWorkerProfileUseCase: GetLocalMyWorkerProfileUseCase,
    private val getJobPostingsUseCase: GetJobPostingsUseCase,
    private val getCrawlingJobPostingsUseCase: GetCrawlingJobPostingsUseCase,
    private val applyJobPostingUseCase: ApplyJobPostingUseCase,
    private val addFavoriteJobPostingUseCase: AddFavoriteJobPostingUseCase,
    private val removeFavoriteJobPostingUseCase: RemoveFavoriteJobPostingUseCase,
    private val showNotificationCenterUseCase: ShowNotificationCenterUseCase,
    private val getUnreadNotificationCountUseCase: GetUnreadNotificationCountUseCase,
    private val errorHandler: ErrorHandler,
    val eventHandler: EventHandler,
) : BaseViewModel() {
    private val _profile = MutableStateFlow<WorkerProfile?>(null)
    val profile = _profile.asStateFlow()

    private val next = MutableStateFlow<String?>(null)

    private val _jobPostings = MutableStateFlow<List<JobPosting>?>(null)
    val jobPostings = _jobPostings.asStateFlow()

    private val _callType = MutableStateFlow<JobPostingCallType>(JobPostingCallType.IN_APP)
    val callType = _callType.asStateFlow()

    private val _showNotificationCenter = MutableStateFlow(false)
    val showNotificationCenter = _showNotificationCenter.asStateFlow()

    private val _unreadNotificationCount = MutableStateFlow(0)
    val unreadNotificationCount = _unreadNotificationCount.asStateFlow()

    init {
        getJobPostings()
        showNotificationCenter()
        getMyWorkerProfile()
    }

    internal fun getJobPostings() = viewModelScope.launch {
        if (_callType.value == JobPostingCallType.END) return@launch

        when (_callType.value) {
            JobPostingCallType.IN_APP -> fetchInAppJobPostings()
            JobPostingCallType.CRAWLING -> fetchCrawlingJobPostings()
            JobPostingCallType.END -> return@launch
        }
    }

    internal fun getUnreadNotificationCount() = viewModelScope.launch {
        getUnreadNotificationCountUseCase().onSuccess {
            _unreadNotificationCount.value = it
        }.onFailure { errorHandler.sendError(it) }
    }

    internal fun applyJobPosting(jobPostingId: String) = viewModelScope.launch {
        applyJobPostingUseCase(
            jobPostingId = jobPostingId,
            applyMethod = ApplyMethod.APP
        ).onSuccess {
            eventHandler.sendEvent(MainEvent.ShowSnackBar("지원이 완료되었어요.", SnackBarType.SUCCESS))

            _jobPostings.value = _jobPostings.value?.map {
                if (it.jobPostingType == JobPostingType.CAREMEET && it.id == jobPostingId) {
                    val jobPosting = it as WorkerJobPosting
                    jobPosting.copy(applyTime = LocalDateTime.now())
                } else it
            }
        }.onFailure { errorHandler.sendError(it) }
    }

    internal fun addFavoriteJobPosting(
        jobPostingId: String,
        jobPostingType: JobPostingType,
    ) = viewModelScope.launch {
        addFavoriteJobPostingUseCase(
            jobPostingId = jobPostingId,
            jobPostingType = jobPostingType,
        ).onSuccess {
            eventHandler.sendEvent(MainEvent.ShowSnackBar("즐겨찾기에 추가되었어요.", SnackBarType.SUCCESS))

            _jobPostings.value = _jobPostings.value?.map {
                when (it.jobPostingType) {
                    JobPostingType.CAREMEET -> {
                        it as WorkerJobPosting
                        if (it.id == jobPostingId) it.copy(isFavorite = true) else it
                    }

                    else -> {
                        it as CrawlingJobPosting
                        if (it.id == jobPostingId) it.copy(isFavorite = true) else it
                    }
                }
            }
        }.onFailure { errorHandler.sendError(it) }
    }

    internal fun removeFavoriteJobPosting(jobPostingId: String) = viewModelScope.launch {
        removeFavoriteJobPostingUseCase(jobPostingId = jobPostingId).onSuccess {
            eventHandler.sendEvent(MainEvent.ShowSnackBar("즐겨찾기에서 제거되었어요", SnackBarType.SUCCESS))

            _jobPostings.value = _jobPostings.value?.map {
                when (it.jobPostingType) {
                    JobPostingType.CAREMEET -> {
                        it as WorkerJobPosting
                        if (it.id == jobPostingId) it.copy(isFavorite = false) else it
                    }

                    else -> {
                        it as CrawlingJobPosting
                        if (it.id == jobPostingId) it.copy(isFavorite = false) else it
                    }
                }
            }
        }.onFailure { errorHandler.sendError(it) }
    }

    private fun showNotificationCenter() = viewModelScope.launch {
        launch {
            showNotificationCenterUseCase().onSuccess {
                _showNotificationCenter.value = it
            }
        }
    }

    private fun getMyWorkerProfile() = viewModelScope.launch {
        getLocalMyWorkerProfileUseCase().onSuccess {
            _profile.value = it
        }.onFailure {
            eventHandler.sendEvent(MainEvent.ShowSnackBar(it.message.toString()))
        }
    }

    private suspend fun fetchInAppJobPostings() {
        getJobPostingsUseCase(next = next.value).onSuccess { (nextId, postings) ->
            next.value = nextId
            if (nextId == null) {
                _callType.value = JobPostingCallType.CRAWLING
            }
            _jobPostings.value = _jobPostings.value?.plus(postings) ?: postings

            if (_jobPostings.value?.isEmpty() != false) {
                getJobPostings()
            }
        }.onFailure { errorHandler.sendError(it) }
    }

    private suspend fun fetchCrawlingJobPostings() {
        getCrawlingJobPostingsUseCase(next = next.value).onSuccess { (nextId, postings) ->
            next.value = nextId
            if (nextId == null) {
                _callType.value = JobPostingCallType.END
            }
            _jobPostings.value = _jobPostings.value?.plus(postings) ?: postings
        }.onFailure { errorHandler.sendError(it) }
    }
}

enum class JobPostingCallType {
    IN_APP, CRAWLING, END
}