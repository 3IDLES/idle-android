package com.idle.worker.job.posting.detail.center

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.binding.EventHelper
import com.idle.binding.MainEvent
import com.idle.binding.ToastType
import com.idle.common.suspendRunCatching
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.jobposting.CenterJobPostingDetail
import com.idle.domain.model.jobposting.EditJobPostingDetail
import com.idle.domain.model.jobposting.JobPostingStatus
import com.idle.domain.model.jobposting.LifeAssistance
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.repositorry.JobPostingRepository
import com.idle.domain.usecase.profile.GetMyCenterProfileUseCase
import com.idle.job.posting.detail.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterJobPostingDetailViewModel @Inject constructor(
    private val getMyCenterProfileUseCase: GetMyCenterProfileUseCase,
    private val jobPostingRepository: JobPostingRepository,
    private val errorHelper: ErrorHelper,
    val eventHelper: EventHelper,
    val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
    private val _profile = MutableStateFlow<CenterProfile?>(null)
    val profile = _profile.asStateFlow()

    private val _jobPostingDetail = MutableStateFlow<CenterJobPostingDetail?>(null)
    val jobPostingDetail = _jobPostingDetail.asStateFlow()

    private val _applicantsCount = MutableStateFlow<Int>(0)
    val applicantsCount = _applicantsCount.asStateFlow()

    private val _jobPostingState = MutableStateFlow(JobPostingDetailState.SUMMARY)
    val jobPostingState = _jobPostingState.asStateFlow()

    init {
        getMyCenterProfile()
    }

    private fun getMyCenterProfile() = viewModelScope.launch {
        suspendRunCatching {
            getMyCenterProfileUseCase()
        }.onSuccess {
            _profile.value = it
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun getCenterJobPostingDetail(jobPostingId: String) = viewModelScope.launch {
        suspendRunCatching {
            jobPostingRepository.getCenterJobPostingDetail(jobPostingId)
        }.onSuccess { _jobPostingDetail.value = it }
            .onFailure { errorHelper.sendError(it) }
    }

    internal fun getApplicantsCount(jobPostingId: String) = viewModelScope.launch {
        suspendRunCatching {
            jobPostingRepository.getApplicantsCount(jobPostingId)
        }.onSuccess {
            _applicantsCount.value = it
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun setJobPostingState(state: JobPostingDetailState) {
        _jobPostingState.value = state
    }

    internal fun updateJobPosting(editJobPostingDetail: EditJobPostingDetail) =
        viewModelScope.launch {
            if (editJobPostingDetail.applyMethod.isEmpty()) {
                eventHelper.sendEvent(
                    MainEvent.ShowToast("반드시 1개 이상의 지원 방법을 선택해야합니다.")
                )
                return@launch
            }

            suspendRunCatching {
                jobPostingRepository.updateJobPosting(
                    jobPostingId = _jobPostingDetail.value?.id ?: return@suspendRunCatching,
                    weekdays = editJobPostingDetail.weekdays.toList()
                        .sortedBy { it.ordinal },
                    startTime = editJobPostingDetail.startTime,
                    endTime = editJobPostingDetail.endTime,
                    payType = editJobPostingDetail.payType,
                    payAmount = editJobPostingDetail.payAmount.toIntOrNull() ?: return@suspendRunCatching,
                    roadNameAddress = editJobPostingDetail.roadNameAddress,
                    lotNumberAddress = editJobPostingDetail.lotNumberAddress,
                    clientName = editJobPostingDetail.clientName,
                    gender = editJobPostingDetail.gender,
                    birthYear = editJobPostingDetail.birthYear.toIntOrNull() ?: return@suspendRunCatching,
                    weight = editJobPostingDetail.weight?.toIntOrNull(),
                    careLevel = editJobPostingDetail.careLevel.toIntOrNull() ?: return@suspendRunCatching,
                    mentalStatus = editJobPostingDetail.mentalStatus,
                    disease = editJobPostingDetail.disease.ifBlank { null },
                    isMealAssistance = editJobPostingDetail.isMealAssistance,
                    isBowelAssistance = editJobPostingDetail.isBowelAssistance,
                    isWalkingAssistance = editJobPostingDetail.isWalkingAssistance,
                    lifeAssistance = editJobPostingDetail.lifeAssistance.toList()
                        .sortedBy { it.ordinal }
                        .ifEmpty { listOf(LifeAssistance.NONE) },
                    extraRequirement = editJobPostingDetail.extraRequirement,
                    isExperiencePreferred = editJobPostingDetail.isExperiencePreferred,
                    applyMethod = editJobPostingDetail.applyMethod.toList()
                        .sortedBy { it.ordinal },
                    applyDeadLineType = editJobPostingDetail.applyDeadlineType,
                    applyDeadline = editJobPostingDetail.applyDeadline.toString()
                        .ifBlank { null },
                )
            }.onSuccess {
                getCenterJobPostingDetail(_jobPostingDetail.value?.id ?: return@launch)
                eventHelper.sendEvent(
                    MainEvent.ShowToast("수정이 완료되었어요.", ToastType.SUCCESS)
                )
                _jobPostingState.value = JobPostingDetailState.SUMMARY
            }.onFailure { errorHelper.sendError(it) }
        }

    internal fun endJobPosting(jobPostingId: String) = viewModelScope.launch {
        suspendRunCatching {
            jobPostingRepository.endJobPosting(jobPostingId)
        }.onSuccess {
            _jobPostingDetail.value =
                _jobPostingDetail.value?.copy(jobPostingStatus = JobPostingStatus.COMPLETED)
            eventHelper.sendEvent(
                MainEvent.ShowToast("채용을 종료했어요.", ToastType.SUCCESS)
            )
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun deleteJobPosting(jobPostingId: String) = viewModelScope.launch {
        suspendRunCatching {
            jobPostingRepository.deleteJobPosting(jobPostingId)
        }.onSuccess {
            navigationHelper.navigateTo(
                com.idle.navigation.NavigationEvent.To(
                    com.idle.navigation.DeepLinkDestination.CenterHome,
                    R.id.centerJobPostingDetailFragment
                )
            )
        }.onFailure { errorHelper.sendError(it) }
    }
}

enum class JobPostingDetailState(val step: Int) {
    SUMMARY(1),
    EDIT(2),
    PREVIEW(3),
    ;
}
