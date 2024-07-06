package com.idle.auth.center

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.common_ui.DeepLinkDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterAuthViewModel @Inject constructor() : ViewModel() {
    private val _eventFlow = MutableSharedFlow<CenterAuthEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    internal fun event(event: CenterAuthEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }
}

sealed class CenterAuthEvent {
    data class NavigateTo(val destination: DeepLinkDestination) : CenterAuthEvent()
}