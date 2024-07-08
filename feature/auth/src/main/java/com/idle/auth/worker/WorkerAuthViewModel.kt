package com.idle.auth.center

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerAuthViewModel @Inject constructor() : ViewModel() {
    private val _eventFlow = MutableSharedFlow<WorkerAuthEvent>()
    internal val eventFlow = _eventFlow.asSharedFlow()

    internal fun event(event: WorkerAuthEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }
}

sealed class WorkerAuthEvent {
    data class NavigateTo(val destination: com.idle.binding.DeepLinkDestination) : WorkerAuthEvent()
}