package com.idle.presentation.forceupdate

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.config.ForceUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForceUpdateViewModel @Inject constructor() : BaseViewModel() {
    private val _forceUpdateEvent = MutableSharedFlow<ForceUpdateEvent>()
    val forceUpdateEvent = _forceUpdateEvent.asSharedFlow()

    private val _forceUpdate = MutableStateFlow<ForceUpdate?>(null)
    val forceUpdate = _forceUpdate.asStateFlow()

    fun dismiss() = event(ForceUpdateEvent.Dismiss)
    fun update() = event(ForceUpdateEvent.Update)

    internal fun setForceUpdate(forceUpdate: ForceUpdate) {
        _forceUpdate.value = forceUpdate
    }

    private fun event(event: ForceUpdateEvent) = viewModelScope.launch {
        _forceUpdateEvent.emit(event)
    }
}

sealed class ForceUpdateEvent {
    data object Dismiss : ForceUpdateEvent()
    data object Update : ForceUpdateEvent()
}