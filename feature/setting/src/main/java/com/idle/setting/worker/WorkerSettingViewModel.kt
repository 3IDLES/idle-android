package com.idle.setting.worker

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
class WorkerSettingViewModel @Inject constructor() : BaseViewModel() {
    private val _workerSettingEvent = MutableSharedFlow<SettingEvent>()
    val workerSettingEvent = _workerSettingEvent.asSharedFlow()
    fun logout() {
        Log.d("test", "로그아웃 호출")
    }

    fun clickLogout() = workerSettingEvent(SettingEvent.Logout)
    fun clickWithdrawal() = workerSettingEvent(SettingEvent.Withdrawal)
    fun clickProfile() = workerSettingEvent(SettingEvent.Profile)

    private fun workerSettingEvent(event: SettingEvent) = viewModelScope.launch {
        _workerSettingEvent.emit(event)
    }
}