package com.idle.signin.worker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.common_ui.DeepLinkDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerSignUpViewModel @Inject constructor() : ViewModel() {
    private val _eventFlow = MutableSharedFlow<WorkerSignUpEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    internal fun event(event: WorkerSignUpEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }
}

sealed class WorkerSignUpEvent {
    data class NavigateTo(val destination: DeepLinkDestination) : WorkerSignUpEvent()
}