package com.idle.signin.center

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.common_ui.DeepLinkDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterSignInViewModel @Inject constructor() : ViewModel() {
    private val _eventFlow = MutableSharedFlow<CenterSignInEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    internal fun event(event: CenterSignInEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }
}

sealed class CenterSignInEvent {
    data class NavigateTo(val destination: DeepLinkDestination) : CenterSignInEvent()
}