package com.idle.worker.job.posting.detail.worker

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerJobPostingDetailViewModel @Inject constructor() : BaseViewModel() {
    private val _eventFlow = MutableSharedFlow<WorkerJobPostingDetailEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    internal fun workerJobPostingDetailEvent(event: WorkerJobPostingDetailEvent) =
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
}

sealed class WorkerJobPostingDetailEvent {
    data class CallInquiry(val number: String) : WorkerJobPostingDetailEvent()
}