package com.idle.setting.center

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.setting.SettingEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterSettingViewModel @Inject constructor() : BaseViewModel() {
    private val _centerSettingEvent = MutableSharedFlow<SettingEvent>()
    val centerSettingEvent = _centerSettingEvent.asSharedFlow()

    fun logout() {
        Log.d("test", "로그아웃")
    }

    fun clickLogout() = centerSettingEvent(SettingEvent.Logout)
    fun clickWithdrawal() = centerSettingEvent(SettingEvent.Withdrawal)
    fun clickCenterProfile() = centerSettingEvent(SettingEvent.Profile)

    private fun centerSettingEvent(event: SettingEvent) = viewModelScope.launch {
        _centerSettingEvent.emit(event)
    }
}