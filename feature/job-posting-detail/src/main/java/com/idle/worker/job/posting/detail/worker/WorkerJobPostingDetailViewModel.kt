package com.idle.worker.job.posting.detail.worker

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.jobposting.WorkerJobPostingDetail
import com.idle.domain.usecase.jobposting.ApplyJobPostingUseCase
import com.idle.domain.usecase.jobposting.GetWorkerJobPostingDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerJobPostingDetailViewModel @Inject constructor(
    private val getWorkerJobPostingDetailUseCase: GetWorkerJobPostingDetailUseCase,
    private val applyJobPostingUseCase: ApplyJobPostingUseCase,
) : BaseViewModel() {
    private val _eventFlow = MutableSharedFlow<WorkerJobPostingDetailEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _workerJobPostingDetail = MutableStateFlow<WorkerJobPostingDetail?>(null)
    val workerJobPostingDetail = _workerJobPostingDetail.asStateFlow()

    internal fun workerJobPostingDetailEvent(event: WorkerJobPostingDetailEvent) =
        viewModelScope.launch { _eventFlow.emit(event) }

    internal fun getJobPostingDetail(jobPostingId: String) = viewModelScope.launch {
        getWorkerJobPostingDetailUseCase(jobPostingId).onSuccess {
            _workerJobPostingDetail.value = it
        }.onFailure {

        }
    }

    fun applyJobPosting(jobPostingId: String, applyMethod: ApplyMethod) = viewModelScope.launch {
        applyJobPostingUseCase(
            jobPostingId = jobPostingId,
            applyMethod = applyMethod,
        ).onSuccess {

        }.onFailure {
            baseEvent(CareBaseEvent.Error(it.message.toString()))
        }
    }
}

sealed class WorkerJobPostingDetailEvent {
    data class CallInquiry(val number: String) : WorkerJobPostingDetailEvent()
}