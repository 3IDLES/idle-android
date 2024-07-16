package com.idle.binding.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val _baseEventFlow = MutableSharedFlow<CareBaseEvent>()
    val baseEventFlow = _baseEventFlow.asSharedFlow()

    fun baseEvent(event: CareBaseEvent) = viewModelScope.launch {
        _baseEventFlow.emit(event)
    }
}

sealed class CareBaseEvent {
    data class NavigateTo(val destination: DeepLinkDestination) : CareBaseEvent()
}