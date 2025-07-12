package com.idle.worker.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.binding.EventHelper
import com.idle.binding.MainEvent
import com.idle.binding.ToastType
import com.idle.common.suspendRunCatching
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.CrawlingJobPosting
import com.idle.domain.model.jobposting.JobPosting
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.jobposting.WorkerJobPosting
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.repositorry.JobPostingRepository
import com.idle.domain.repositorry.NotificationRepository
import com.idle.domain.usecase.profile.GetMyWorkerProfileUseCase
import com.idle.navigation.DeepLinkDestination
import com.idle.navigation.NavigationEvent
import com.idle.navigation.NavigationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorkerHomeViewModel @Inject constructor(
    private val getMyWorkerProfileUseCase: GetMyWorkerProfileUseCase,
    private val notificationRepository: NotificationRepository,
    private val jobPostingRepository: JobPostingRepository,
    private val errorHelper: ErrorHelper,
    private val eventHelper: EventHelper,
    val navigationHelper: NavigationHelper,
) : ViewModel() {
    private val _profile = MutableStateFlow<WorkerProfile?>(null)
    val profile = _profile.asStateFlow()

    private var nextCursorId: String? = null
    private var nextDistance: Int = 15

    private val _jobPostings = MutableStateFlow<List<JobPosting>?>(null)
    val jobPostings = _jobPostings.asStateFlow()

    private val _callType = MutableStateFlow<JobPostingCallType>(JobPostingCallType.IN_APP)
    val callType = _callType.asStateFlow()

    private val _unreadNotificationCount = MutableStateFlow(0)
    val unreadNotificationCount = _unreadNotificationCount.asStateFlow()

    init {
        getJobPostings()
        navigateToSharedJobPosting()
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
        suspendRunCatching {
            notificationRepository.getUnreadNotificationCount()
        }.onSuccess {
            _unreadNotificationCount.value = it
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun applyJobPosting(jobPostingId: String) = viewModelScope.launch {
        suspendRunCatching {
            jobPostingRepository.applyJobPosting(
                jobPostingId = jobPostingId,
                applyMethod = ApplyMethod.APP
            )
        }.onSuccess {
            eventHelper.sendEvent(MainEvent.ShowToast("지원이 완료되었어요.", ToastType.SUCCESS))

            _jobPostings.value = _jobPostings.value?.map {
                if (it.jobPostingType == JobPostingType.CAREMEET && it.id == jobPostingId) {
                    val jobPosting = it as WorkerJobPosting
                    jobPosting.copy(applyTime = LocalDateTime.now())
                } else it
            }
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun addFavoriteJobPosting(
        jobPostingId: String,
        jobPostingType: JobPostingType,
    ) = viewModelScope.launch {
        suspendRunCatching {
            jobPostingRepository.addFavoriteJobPosting(
                jobPostingId = jobPostingId,
                jobPostingType = jobPostingType,
            )
        }.onSuccess {
            eventHelper.sendEvent(MainEvent.ShowToast("즐겨찾기에 추가되었어요.", ToastType.SUCCESS))

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
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun removeFavoriteJobPosting(jobPostingId: String) = viewModelScope.launch {
        suspendRunCatching {
            jobPostingRepository.removeFavoriteJobPosting(jobPostingId = jobPostingId)
        }.onSuccess {
            eventHelper.sendEvent(MainEvent.ShowToast("즐겨찾기에서 제거되었어요", ToastType.SUCCESS))

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
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun getMyWorkerProfile() = viewModelScope.launch {
        suspendRunCatching {
            getMyWorkerProfileUseCase()
        }.onSuccess {
            _profile.value = it
        }.onFailure {
            eventHelper.sendEvent(MainEvent.ShowToast(it.message.toString()))
        }
    }

    private fun navigateToSharedJobPosting() {
        val sharedJobPostingInfo = jobPostingRepository.sharedJobPostingInfo ?: return

        navigationHelper.navigateTo(
            NavigationEvent.To(
                DeepLinkDestination.WorkerJobDetail(
                    jobPostingId = sharedJobPostingInfo.jobPostingId,
                    jobPostingType = sharedJobPostingInfo.jobPostingType.name,
                )
            )
        )
    }

    private suspend fun fetchInAppJobPostings() {
        suspendRunCatching {
            jobPostingRepository.getJobPostings(next = nextCursorId)
        }.onSuccess { (nextId, postings) ->
            nextCursorId = nextId
            if (nextId == null) {
                _callType.value = JobPostingCallType.CRAWLING
            }
            _jobPostings.value = _jobPostings.value?.plus(postings) ?: postings

            if (_jobPostings.value?.isEmpty() != false) {
                getJobPostings()
            }
        }.onFailure { errorHelper.sendError(it) }
    }

    private suspend fun fetchCrawlingJobPostings() {
        suspendRunCatching {
            jobPostingRepository.getCrawlingJobPostings(
                next = nextCursorId,
                distance = nextDistance,
            )
        }.onSuccess { pageInfo ->
            nextCursorId = pageInfo.nextCursor
            nextDistance = pageInfo.nextDistance

            if (pageInfo.nextCursor == null) {
                _callType.value = JobPostingCallType.END
            }
            _jobPostings.value = _jobPostings.value?.plus(pageInfo.items) ?: pageInfo.items
        }.onFailure { errorHelper.sendError(it) }
    }
}

enum class JobPostingCallType {
    IN_APP, CRAWLING, END
}
