package com.idle.worker.job.posting.detail.center

import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.jobposting.CenterJobPostingDetail
import com.idle.domain.model.jobposting.EditJobPostingDetail
import com.idle.domain.model.jobposting.JobPostingStatus
import com.idle.domain.model.jobposting.LifeAssistance
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.usecase.jobposting.DeleteJobPostingUseCase
import com.idle.domain.usecase.jobposting.EndJobPostingUseCase
import com.idle.domain.usecase.jobposting.GetApplicantsCountUseCase
import com.idle.domain.usecase.jobposting.GetCenterJobPostingDetailUseCase
import com.idle.domain.usecase.jobposting.UpdateJobPostingUseCase
import com.idle.domain.usecase.profile.GetLocalMyCenterProfileUseCase
import com.idle.job.posting.detail.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterJobPostingDetailViewModel @Inject constructor(
    private val getLocalMyCenterProfileUseCase: GetLocalMyCenterProfileUseCase,
    private val getCenterJobPostingDetailUseCase: GetCenterJobPostingDetailUseCase,
    private val getApplicantsCountUseCase: GetApplicantsCountUseCase,
    private val updateJobPostingUseCase: UpdateJobPostingUseCase,
    private val endJobPostingUseCase: EndJobPostingUseCase,
    private val deleteJobPostingUseCase: DeleteJobPostingUseCase,
) : BaseViewModel() {
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
        getLocalMyCenterProfileUseCase().onSuccess {
            _profile.value = it
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    internal fun getCenterJobPostingDetail(jobPostingId: String) = viewModelScope.launch {
        getCenterJobPostingDetailUseCase(jobPostingId)
            .onSuccess { _jobPostingDetail.value = it }
            .onFailure { handleFailure(it as HttpResponseException) }
    }

    internal fun getApplicantsCount(jobPostingId: String) = viewModelScope.launch {
        getApplicantsCountUseCase(jobPostingId).onSuccess {
            _applicantsCount.value = it
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    internal fun setJobPostingState(state: JobPostingDetailState) {
        _jobPostingState.value = state
    }

    internal fun updateJobPosting(editJobPostingDetail: EditJobPostingDetail) =
        viewModelScope.launch {
            if (editJobPostingDetail.applyMethod.isEmpty()) {
                baseEvent(CareBaseEvent.ShowSnackBar("반드시 1개 이상의 지원 방법을 선택해야합니다.|ERROR"))
                return@launch
            }

            updateJobPostingUseCase(
                jobPostingId = _jobPostingDetail.value?.id ?: return@launch,
                weekdays = editJobPostingDetail.weekdays.toList()
                    .sortedBy { it.ordinal },
                startTime = editJobPostingDetail.startTime,
                endTime = editJobPostingDetail.endTime,
                payType = editJobPostingDetail.payType,
                payAmount = editJobPostingDetail.payAmount.toIntOrNull() ?: return@launch,
                roadNameAddress = editJobPostingDetail.roadNameAddress,
                lotNumberAddress = editJobPostingDetail.lotNumberAddress,
                clientName = editJobPostingDetail.clientName,
                gender = editJobPostingDetail.gender,
                birthYear = editJobPostingDetail.birthYear.toIntOrNull() ?: return@launch,
                weight = editJobPostingDetail.weight?.toIntOrNull(),
                careLevel = editJobPostingDetail.careLevel.toIntOrNull() ?: return@launch,
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
                applyDeadlineType = editJobPostingDetail.applyDeadlineType,
                applyDeadline = editJobPostingDetail.applyDeadline.toString()
                    .ifBlank { null },
            ).onSuccess {
                baseEvent(CareBaseEvent.ShowSnackBar("수정이 완료되었어요.|SUCCESS"))

                _jobPostingDetail.value = CenterJobPostingDetail(
                    id = _jobPostingDetail.value?.id ?: return@launch,
                    weekdays = editJobPostingDetail.weekdays,
                    startTime = editJobPostingDetail.startTime,
                    endTime = editJobPostingDetail.endTime,
                    payType = editJobPostingDetail.payType,
                    payAmount = editJobPostingDetail.payAmount.toIntOrNull() ?: return@launch,
                    roadNameAddress = editJobPostingDetail.roadNameAddress,
                    lotNumberAddress = editJobPostingDetail.lotNumberAddress,
                    clientName = editJobPostingDetail.clientName,
                    gender = editJobPostingDetail.gender,
                    age = editJobPostingDetail.birthYear.toIntOrNull() ?: return@launch,
                    weight = editJobPostingDetail.weight?.toInt(),
                    careLevel = editJobPostingDetail.careLevel.toIntOrNull() ?: return@launch,
                    mentalStatus = editJobPostingDetail.mentalStatus,
                    disease = editJobPostingDetail.disease,
                    isMealAssistance = editJobPostingDetail.isMealAssistance,
                    isBowelAssistance = editJobPostingDetail.isBowelAssistance,
                    isWalkingAssistance = editJobPostingDetail.isWalkingAssistance,
                    lifeAssistance = editJobPostingDetail.lifeAssistance,
                    extraRequirement = editJobPostingDetail.extraRequirement,
                    isExperiencePreferred = editJobPostingDetail.isExperiencePreferred,
                    applyMethod = editJobPostingDetail.applyMethod,
                    applyDeadlineType = editJobPostingDetail.applyDeadlineType,
                    applyDeadline = editJobPostingDetail.applyDeadline,
                    jobPostingStatus = _jobPostingDetail.value?.jobPostingStatus ?: return@launch,
                )

                _jobPostingState.value = JobPostingDetailState.SUMMARY
            }.onFailure { handleFailure(it as HttpResponseException) }
        }

    internal fun endJobPosting(jobPostingId: String) = viewModelScope.launch {
        endJobPostingUseCase(jobPostingId).onSuccess {
            _jobPostingDetail.value =
                _jobPostingDetail.value?.copy(jobPostingStatus = JobPostingStatus.COMPLETED)
            baseEvent(CareBaseEvent.ShowSnackBar("채용을 종료했어요.|SUCCESS"))
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    internal fun deleteJobPosting(jobPostingId: String) = viewModelScope.launch {
        deleteJobPostingUseCase(jobPostingId).onSuccess {
            baseEvent(
                CareBaseEvent.NavigateTo(
                    DeepLinkDestination.CenterHome,
                    R.id.centerJobPostingDetailFragment
                )
            )
        }.onFailure { handleFailure(it as HttpResponseException) }
    }
}

enum class JobPostingDetailState(val step: Int) {
    SUMMARY(1),
    EDIT(2),
    PREVIEW(3),
    ;
}