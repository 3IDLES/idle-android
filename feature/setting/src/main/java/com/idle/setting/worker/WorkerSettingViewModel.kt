package com.idle.setting.worker

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerSettingViewModel @Inject constructor() : BaseViewModel() {
    private val _workerSettingEvent = MutableSharedFlow<WorkerSettingEvent>()
    val workerSettingEvent = _workerSettingEvent.asSharedFlow()
    fun logout() {
        Log.d("test", "로그아웃 호출")
    }

    fun clickLogout() = workerSettingEvent(WorkerSettingEvent.Logout)
    fun clickWithdrawal() = workerSettingEvent(WorkerSettingEvent.Withdrawal)
    fun clickProfile() = workerSettingEvent(WorkerSettingEvent.MyProfile)

    private fun workerSettingEvent(event: WorkerSettingEvent) = viewModelScope.launch {
        _workerSettingEvent.emit(event)
    }
}

sealed class WorkerSettingEvent {
    data object MyProfile : WorkerSettingEvent()
    data object Logout : WorkerSettingEvent()
    data object Withdrawal : WorkerSettingEvent()
}