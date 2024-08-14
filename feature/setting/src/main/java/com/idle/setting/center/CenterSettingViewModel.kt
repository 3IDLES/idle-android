package com.idle.setting.center

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterSettingViewModel @Inject constructor() : BaseViewModel() {
    private val _centerSettingEvent = MutableSharedFlow<CenterSettingEvent>()
    val centerSettingEvent = _centerSettingEvent.asSharedFlow()

    fun logout() = centerSettingEvent(CenterSettingEvent.Logout)
    fun withdrawal() = centerSettingEvent(CenterSettingEvent.Withdrawal)
    fun myCenter() = centerSettingEvent(CenterSettingEvent.MyCenter)

    private fun centerSettingEvent(event: CenterSettingEvent) = viewModelScope.launch {
        _centerSettingEvent.emit(event)
    }
}

sealed class CenterSettingEvent {
    data object MyCenter : CenterSettingEvent()
    data object Logout : CenterSettingEvent()
    data object Withdrawal : CenterSettingEvent()
}