package com.idle.worker.job.posting.detail.center

import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.jobposting.LifeAssistance
import com.idle.domain.model.jobposting.CenterJobPostingDetail
import com.idle.domain.model.jobposting.EditJobPostingDetail
import com.idle.domain.usecase.jobposting.EndJobPostingUseCase
import com.idle.domain.usecase.jobposting.GetApplicantsCountUseCase
import com.idle.domain.usecase.jobposting.GetCenterJobPostingDetailUseCase
import com.idle.domain.usecase.jobposting.UpdateJobPostingUseCase
import com.idle.job.posting.detail.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterJobPostingDetailViewModel @Inject constructor(
    private val getCenterJobPostingDetailUseCase: GetCenterJobPostingDetailUseCase,
    private val getApplicantsCountUseCase: GetApplicantsCountUseCase,
    private val updateJobPostingUseCase: UpdateJobPostingUseCase,
    private val endJobPostingUseCase: EndJobPostingUseCase,
) : BaseViewModel() {
    private val _jobPostingDetail = MutableStateFlow<CenterJobPostingDetail?>(null)
    val jobPostingDetail = _jobPostingDetail.asStateFlow()

    private val _applicantsCount = MutableStateFlow<Int>(0)
    val applicantsCount = _applicantsCount.asStateFlow()

    private val _isEditState = MutableStateFlow(false)
    val isEditState = _isEditState.asStateFlow()

    fun getCenterJobPostingDetail(jobPostingId: String) = viewModelScope.launch {
        getCenterJobPostingDetailUseCase(jobPostingId)
            .onSuccess { _jobPostingDetail.value = it }
            .onFailure { handleFailure(it as HttpResponseException) }
    }

    fun getApplicantsCount(jobPostingId: String) = viewModelScope.launch {
        getApplicantsCountUseCase(jobPostingId).onSuccess {
            _applicantsCount.value = it
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    fun setEditState(state: Boolean) {
        _isEditState.value = state
    }

    fun updateJobPosting(editJobPostingDetail: EditJobPostingDetail) = viewModelScope.launch {
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
            applyDeadline = editJobPostingDetail.applyDeadline.toString().ifBlank { null },
        ).onSuccess {
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
            )

            _isEditState.value = false
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    fun endJobPosting(jobPostingId: String) = viewModelScope.launch {
        endJobPostingUseCase(jobPostingId).onSuccess {
            baseEvent(
                CareBaseEvent.NavigateTo(
                    DeepLinkDestination.CenterHome,
                    R.id.centerJobPostingDetailFragment
                )
            )
        }.onFailure { handleFailure(it as HttpResponseException) }
    }
}