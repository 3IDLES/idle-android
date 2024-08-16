package com.idle.worker.job.posting.detail.center

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.job.LifeAssistance
import com.idle.domain.model.jobposting.CenterJobPostingDetail
import com.idle.domain.model.jobposting.EditJobPostingDetail
import com.idle.domain.usecase.jobposting.GetCenterJobPostingDetailUseCase
import com.idle.domain.usecase.jobposting.UpdateJobPostingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterJobPostingDetailViewModel @Inject constructor(
    private val getCenterJobPostingDetailUseCase: GetCenterJobPostingDetailUseCase,
    private val updateJobPostingUseCase: UpdateJobPostingUseCase,
) : BaseViewModel() {
    private val _jobPostingDetail = MutableStateFlow<CenterJobPostingDetail?>(null)
    val jobPostingDetail = _jobPostingDetail.asStateFlow()

    private val _isEditState = MutableStateFlow(false)
    val isEditState = _isEditState.asStateFlow()

    fun getCenterJobPostingDetail(jobPostingId: String) = viewModelScope.launch {
        getCenterJobPostingDetailUseCase(jobPostingId)
            .onSuccess { _jobPostingDetail.value = it }
            .onFailure { Log.d("test", "센터 공고 상세 조회 실패! $it") }
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
        }.onFailure {

        }
    }
}