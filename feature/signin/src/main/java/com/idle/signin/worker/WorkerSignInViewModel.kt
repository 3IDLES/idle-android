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
class WorkerSignInViewModel @Inject constructor() : ViewModel() {
    private val _eventFlow = MutableSharedFlow<WorkerSignInEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    internal fun event(event: WorkerSignInEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }
}

sealed class WorkerSignInEvent {
    data class NavigateTo(val destination: DeepLinkDestination) : WorkerSignInEvent()
}