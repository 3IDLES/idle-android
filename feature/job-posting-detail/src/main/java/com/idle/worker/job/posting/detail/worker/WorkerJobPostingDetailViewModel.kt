package com.idle.worker.job.posting.detail.worker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.analytics.AnalyticsEvent
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.ACTION_NAME
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.SCREEN_NAME
import com.idle.analytics.AnalyticsEvent.Types.ACTION
import com.idle.analytics.AnalyticsHelper
import com.idle.binding.EventHelper
import com.idle.binding.MainEvent
import com.idle.binding.ToastType.SUCCESS
import com.idle.common.suspendRunCatching
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.CrawlingJobPostingDetail
import com.idle.domain.model.jobposting.JobPosting
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.jobposting.WorkerJobPostingDetail
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.repositorry.ChatRepository
import com.idle.domain.repositorry.JobPostingRepository
import com.idle.domain.usecase.profile.GetMyWorkerProfileUseCase
import com.idle.navigation.DeepLinkDestination
import com.idle.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorkerJobPostingDetailViewModel @Inject constructor(
    private val getMyWorkerProfileUseCase: GetMyWorkerProfileUseCase,
    private val jobPostingRepository: JobPostingRepository,
    private val chatRepository: ChatRepository,
    private val analyticsHelper: AnalyticsHelper,
    private val errorHelper: ErrorHelper,
    val eventHelper: EventHelper,
    val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
    private val _profile = MutableStateFlow<WorkerProfile?>(null)
    val profile = _profile.asStateFlow()

    private val _workerJobPostingDetail = MutableStateFlow<JobPosting?>(null)
    val workerJobPostingDetail = _workerJobPostingDetail.asStateFlow()

    internal fun getMyProfile() = viewModelScope.launch {
        suspendRunCatching {
            getMyWorkerProfileUseCase()
        }.onSuccess {
            _profile.value = it
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun getJobPostingDetail(
        jobPostingId: String,
        jobPostingType: String,
    ) = viewModelScope.launch {
        when (jobPostingType) {
            JobPostingType.CAREMEET.name -> suspendRunCatching {
                jobPostingRepository.getWorkerJobPostingDetail(jobPostingId)
            }.onSuccess {
                _workerJobPostingDetail.value = it
            }.onFailure { errorHelper.sendError(it) }

            JobPostingType.WORKNET.name -> suspendRunCatching {
                jobPostingRepository.getCrawlingJobPostingDetail(jobPostingId)
            }.onSuccess {
                _workerJobPostingDetail.value = it
            }.onFailure { errorHelper.sendError(it) }
        }
    }

    internal fun applyJobPosting(jobPostingId: String, applyMethod: ApplyMethod) =
        viewModelScope.launch {
            suspendRunCatching {
                jobPostingRepository.applyJobPosting(
                    jobPostingId = jobPostingId,
                    applyMethod = applyMethod,
                )
            }.onSuccess {
                eventHelper.sendEvent(MainEvent.ShowToast("지원이 완료되었어요.", SUCCESS))

                if (_workerJobPostingDetail.value?.jobPostingType == JobPostingType.CAREMEET) {
                    _workerJobPostingDetail.value =
                        (_workerJobPostingDetail.value as WorkerJobPostingDetail).copy(applyTime = LocalDateTime.now())
                }

                analyticsHelper.logEvent(
                    AnalyticsEvent(
                        type = ACTION,
                        properties = mutableMapOf(
                            ACTION_NAME to "apply_job_posting",
                            SCREEN_NAME to "carer_job_posting_detail",
                            "apply_method" to applyMethod.name.lowercase(),
                        )
                    )
                )
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
            eventHelper.sendEvent(
                MainEvent.ShowToast("즐겨찾기에 추가되었어요.", SUCCESS)
            )

            when (jobPostingType) {
                JobPostingType.CAREMEET -> {
                    _workerJobPostingDetail.value =
                        (_workerJobPostingDetail.value as WorkerJobPostingDetail).copy(isFavorite = true)
                }

                else -> {
                    _workerJobPostingDetail.value =
                        (_workerJobPostingDetail.value as CrawlingJobPostingDetail).copy(isFavorite = true)
                }
            }
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun removeFavoriteJobPosting(
        jobPostingId: String,
        jobPostingType: JobPostingType,
    ) = viewModelScope.launch {
        suspendRunCatching {
            jobPostingRepository.removeFavoriteJobPosting(jobPostingId = jobPostingId)
        }.onSuccess {
            eventHelper.sendEvent(
                MainEvent.ShowToast("즐겨찾기에서 제거되었어요.", SUCCESS)
            )

            when (jobPostingType) {
                JobPostingType.CAREMEET -> {
                    _workerJobPostingDetail.value =
                        (_workerJobPostingDetail.value as WorkerJobPostingDetail).copy(isFavorite = false)
                }

                else -> {
                    _workerJobPostingDetail.value =
                        (_workerJobPostingDetail.value as CrawlingJobPostingDetail).copy(isFavorite = false)
                }
            }
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun generateChatRoom(opponentId: String) = viewModelScope.launch {
        suspendRunCatching {
            chatRepository.generateChatRooms(
                userType = UserType.WORKER,
                opponentId = opponentId,
            )
        }.onSuccess {
            navigationHelper.navigateTo(
                NavigationEvent.To(
                    DeepLinkDestination.ChattingDetail(
                        chattingRoomId = it,
                        myId = _profile.value?.workerId ?: return@onSuccess,
                        myUserType = UserType.WORKER.apiValue,
                        opponentId = opponentId,
                        unReadMessageCount = -1,
                        fromJobPosting = true,
                    )
                )
            )
        }.onFailure { errorHelper.sendError(it) }
    }
}
