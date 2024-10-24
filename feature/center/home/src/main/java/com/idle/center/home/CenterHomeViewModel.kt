package com.idle.center.home

import androidx.lifecycle.viewModelScope
import com.idle.binding.EventHandlerHelper
import com.idle.binding.MainEvent
import com.idle.binding.NavigationHelper
import com.idle.binding.ToastType.SUCCESS
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.error.ErrorHandlerHelper
import com.idle.domain.model.jobposting.CenterJobPosting
import com.idle.domain.usecase.config.ShowNotificationCenterUseCase
import com.idle.domain.usecase.jobposting.EndJobPostingUseCase
import com.idle.domain.usecase.jobposting.GetJobPostingsCompletedUseCase
import com.idle.domain.usecase.jobposting.GetJobPostingsInProgressUseCase
import com.idle.domain.usecase.notification.GetUnreadNotificationCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterHomeViewModel @Inject constructor(
    private val getJobPostingsInProgressUseCase: GetJobPostingsInProgressUseCase,
    private val getJobPostingsCompletedUseCase: GetJobPostingsCompletedUseCase,
    private val endJobPostingUseCase: EndJobPostingUseCase,
    private val showNotificationCenterUseCase: ShowNotificationCenterUseCase,
    private val getUnreadNotificationCountUseCase: GetUnreadNotificationCountUseCase,
    private val errorHandlerHelper: ErrorHandlerHelper,
    private val eventHandlerHelper: EventHandlerHelper,
    val navigationHelper: NavigationHelper,
) : BaseViewModel() {
    private val _recruitmentPostStatus = MutableStateFlow(RecruitmentPostStatus.IN_PROGRESS)
    val recruitmentPostStatus = _recruitmentPostStatus.asStateFlow()

    private val _jobPostingsInProgress = MutableStateFlow<List<CenterJobPosting>?>(null)
    val jobPostingsInProgress = _jobPostingsInProgress.asStateFlow()

    private val _jobPostingsCompleted = MutableStateFlow<List<CenterJobPosting>?>(null)
    val jobPostingsCompleted = _jobPostingsCompleted.asStateFlow()

    private val _unreadNotificationCount = MutableStateFlow(0)
    val unreadNotificationCount = _unreadNotificationCount.asStateFlow()

    private val _showNotificationCenter = MutableStateFlow(false)
    val showNotificationCenter = _showNotificationCenter.asStateFlow()

    init {
        showNotificationCenter()
    }

    private fun showNotificationCenter() = viewModelScope.launch {
        showNotificationCenterUseCase().onSuccess {
            _showNotificationCenter.value = it
        }
    }

    internal fun getUnreadNotificationCount() = viewModelScope.launch {
        getUnreadNotificationCountUseCase().onSuccess {
            _unreadNotificationCount.value = it
        }.onFailure { errorHandlerHelper.sendError(it) }
    }

    internal fun setRecruitmentPostStatus(recruitmentPostStatus: RecruitmentPostStatus) {
        _recruitmentPostStatus.value = recruitmentPostStatus
    }

    internal fun clearJobPostingStatus() {
        _jobPostingsInProgress.value = null
        _jobPostingsCompleted.value = null
    }

    internal fun getJobPostingsInProgress() = viewModelScope.launch {
        getJobPostingsInProgressUseCase().onSuccess {
            _jobPostingsInProgress.value = it
        }.onFailure { errorHandlerHelper.sendError(it) }
    }

    internal fun getJobPostingsCompleted() = viewModelScope.launch {
        getJobPostingsCompletedUseCase().onSuccess {
            _jobPostingsCompleted.value = it
        }.onFailure { errorHandlerHelper.sendError(it) }
    }

    internal fun endJobPosting(jobPostingId: String) = viewModelScope.launch {
        endJobPostingUseCase(jobPostingId).onSuccess {
            val jobPostingsInProgress = _jobPostingsInProgress.value ?: emptyList()
            val jobPostingsCompleted = _jobPostingsCompleted.value ?: emptyList()

            val completedJobPosting = jobPostingsInProgress.firstOrNull { it.id == jobPostingId }

            if (completedJobPosting != null) {
                _jobPostingsCompleted.value = jobPostingsCompleted + completedJobPosting

                _jobPostingsInProgress.value = jobPostingsInProgress.filter {
                    it.id != jobPostingId
                }

                eventHandlerHelper.sendEvent(MainEvent.ShowToast("채용을 종료했어요.", SUCCESS))
            } else {
                eventHandlerHelper.sendEvent(MainEvent.ShowToast("채용 종료에 실패했어요."))
            }
        }.onFailure { errorHandlerHelper.sendError(it) }
    }
}

enum class RecruitmentPostStatus(val displayName: String) {
    IN_PROGRESS("진행 중인 공고"),
    COMPLETED("이전 공고")
}