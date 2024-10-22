package com.idle.worker.job.posting.detail.worker

import androidx.lifecycle.viewModelScope
import com.idle.analytics.AnalyticsEvent
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.ACTION_NAME
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.SCREEN_NAME
import com.idle.analytics.AnalyticsEvent.Types.ACTION
import com.idle.analytics.helper.AnalyticsHelper
import com.idle.binding.EventHandlerHelper
import com.idle.binding.MainEvent
import com.idle.binding.NavigationHelper
import com.idle.binding.ToastType.SUCCESS
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.error.ErrorHandlerHelper
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.CrawlingJobPostingDetail
import com.idle.domain.model.jobposting.JobPosting
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.jobposting.WorkerJobPostingDetail
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.usecase.jobposting.AddFavoriteJobPostingUseCase
import com.idle.domain.usecase.jobposting.ApplyJobPostingUseCase
import com.idle.domain.usecase.jobposting.GetCrawlingJobPostingsDetailUseCase
import com.idle.domain.usecase.jobposting.GetWorkerJobPostingDetailUseCase
import com.idle.domain.usecase.jobposting.RemoveFavoriteJobPostingUseCase
import com.idle.domain.usecase.profile.GetLocalMyWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorkerJobPostingDetailViewModel @Inject constructor(
    private val getLocalMyWorkerProfileUseCase: GetLocalMyWorkerProfileUseCase,
    private val getWorkerJobPostingDetailUseCase: GetWorkerJobPostingDetailUseCase,
    private val getCrawlingJobPostingsDetailUseCase: GetCrawlingJobPostingsDetailUseCase,
    private val applyJobPostingUseCase: ApplyJobPostingUseCase,
    private val addFavoriteJobPostingUseCase: AddFavoriteJobPostingUseCase,
    private val removeFavoriteJobPostingUseCase: RemoveFavoriteJobPostingUseCase,
    private val analyticsHelper: AnalyticsHelper,
    private val errorHandlerHelper: ErrorHandlerHelper,
    val eventHandlerHelper: EventHandlerHelper,
    val navigationHelper: NavigationHelper,
) : BaseViewModel() {
    private val _profile = MutableStateFlow<WorkerProfile?>(null)
    val profile = _profile.asStateFlow()

    private val _workerJobPostingDetail = MutableStateFlow<JobPosting?>(null)
    val workerJobPostingDetail = _workerJobPostingDetail.asStateFlow()

    internal fun getMyProfile() = viewModelScope.launch {
        getLocalMyWorkerProfileUseCase().onSuccess {
            _profile.value = it
        }.onFailure { errorHandlerHelper.sendError(it) }
    }

    internal fun getJobPostingDetail(
        jobPostingId: String,
        jobPostingType: String,
    ) = viewModelScope.launch {
        when (jobPostingType) {
            JobPostingType.CAREMEET.name -> getWorkerJobPostingDetailUseCase(jobPostingId).onSuccess {
                _workerJobPostingDetail.value = it
            }.onFailure { errorHandlerHelper.sendError(it) }

            JobPostingType.WORKNET.name -> getCrawlingJobPostingsDetailUseCase(jobPostingId).onSuccess {
                _workerJobPostingDetail.value = it
            }.onFailure { errorHandlerHelper.sendError(it) }
        }
    }

    internal fun applyJobPosting(jobPostingId: String, applyMethod: ApplyMethod) =
        viewModelScope.launch {
            applyJobPostingUseCase(
                jobPostingId = jobPostingId,
                applyMethod = applyMethod,
            ).onSuccess {
                eventHandlerHelper.sendEvent(MainEvent.ShowToast("지원이 완료되었어요.", SUCCESS))

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
            }.onFailure { errorHandlerHelper.sendError(it) }
        }

    internal fun addFavoriteJobPosting(
        jobPostingId: String,
        jobPostingType: JobPostingType,
    ) = viewModelScope.launch {
        addFavoriteJobPostingUseCase(
            jobPostingId = jobPostingId,
            jobPostingType = jobPostingType,
        ).onSuccess {
            eventHandlerHelper.sendEvent(
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
        }.onFailure { errorHandlerHelper.sendError(it) }
    }

    internal fun removeFavoriteJobPosting(
        jobPostingId: String,
        jobPostingType: JobPostingType,
    ) = viewModelScope.launch {
        removeFavoriteJobPostingUseCase(jobPostingId = jobPostingId).onSuccess {
            eventHandlerHelper.sendEvent(
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
        }.onFailure { errorHandlerHelper.sendError(it) }
    }
}