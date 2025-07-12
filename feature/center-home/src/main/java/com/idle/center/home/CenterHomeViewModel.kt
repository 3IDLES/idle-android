package com.idle.center.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.binding.EventHelper
import com.idle.binding.MainEvent
import com.idle.binding.ToastType.SUCCESS
import com.idle.common.suspendRunCatching
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.jobposting.CenterJobPosting
import com.idle.domain.repositorry.JobPostingRepository
import com.idle.domain.repositorry.NotificationRepository
import com.idle.domain.usecase.jobposting.GetJobPostingsInProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterHomeViewModel @Inject constructor(
    private val jobPostingRepository: JobPostingRepository,
    private val notificationRepository: NotificationRepository,
    private val getJobPostingsInProgressUseCase: GetJobPostingsInProgressUseCase,
    private val errorHelper: ErrorHelper,
    private val eventHelper: EventHelper,
    val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
    private val _recruitmentPostStatus = MutableStateFlow(RecruitmentPostStatus.IN_PROGRESS)
    val recruitmentPostStatus = _recruitmentPostStatus.asStateFlow()

    private val _jobPostingsInProgress = MutableStateFlow<List<CenterJobPosting>?>(null)
    val jobPostingsInProgress = _jobPostingsInProgress.asStateFlow()

    private val _jobPostingsCompleted = MutableStateFlow<List<CenterJobPosting>?>(null)
    val jobPostingsCompleted = _jobPostingsCompleted.asStateFlow()

    private val _unreadNotificationCount = MutableStateFlow(0)
    val unreadNotificationCount = _unreadNotificationCount.asStateFlow()

    internal fun getUnreadNotificationCount() = viewModelScope.launch {
        suspendRunCatching {
            notificationRepository.getUnreadNotificationCount()
        }.onSuccess {
            _unreadNotificationCount.value = it
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun setRecruitmentPostStatus(recruitmentPostStatus: RecruitmentPostStatus) {
        _recruitmentPostStatus.value = recruitmentPostStatus
    }

    internal fun clearJobPostingStatus() {
        _jobPostingsInProgress.value = null
        _jobPostingsCompleted.value = null
    }

    internal fun getJobPostingsInProgress() = viewModelScope.launch {
        suspendRunCatching {
            getJobPostingsInProgressUseCase()
        }.onSuccess {
            _jobPostingsInProgress.value = it
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun getJobPostingsCompleted() = viewModelScope.launch {
        suspendRunCatching {
            jobPostingRepository.getJobPostingsCompleted()
        }.onSuccess {
            _jobPostingsCompleted.value = it
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun endJobPosting(jobPostingId: String) = viewModelScope.launch {
        suspendRunCatching {
            jobPostingRepository.endJobPosting(jobPostingId)
        }.onSuccess {
            val jobPostingsInProgress = _jobPostingsInProgress.value ?: emptyList()
            val jobPostingsCompleted = _jobPostingsCompleted.value ?: emptyList()

            val completedJobPosting = jobPostingsInProgress.firstOrNull { it.id == jobPostingId }

            if (completedJobPosting != null) {
                _jobPostingsCompleted.value = jobPostingsCompleted + completedJobPosting

                _jobPostingsInProgress.value = jobPostingsInProgress.filter {
                    it.id != jobPostingId
                }

                eventHelper.sendEvent(MainEvent.ShowToast("채용을 종료했어요.", SUCCESS))
            } else {
                eventHelper.sendEvent(MainEvent.ShowToast("채용 종료에 실패했어요."))
            }
        }.onFailure { errorHelper.sendError(it) }
    }
}

enum class RecruitmentPostStatus(val displayName: String) {
    IN_PROGRESS("진행 중인 공고"),
    COMPLETED("이전 공고")
}
