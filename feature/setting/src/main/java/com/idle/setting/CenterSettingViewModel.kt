package com.idle.setting

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

    fun logout() = viewModelScope.launch {
        _centerSettingEvent.emit(CenterSettingEvent.Logout)
    }

    fun withdrawal() = viewModelScope.launch {
        _centerSettingEvent.emit(CenterSettingEvent.Withdrawal)
    }
}

sealed class CenterSettingEvent() {
    data object Logout : CenterSettingEvent()
    data object Withdrawal : CenterSettingEvent()
}