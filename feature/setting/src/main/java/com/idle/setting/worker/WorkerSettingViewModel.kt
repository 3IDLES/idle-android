package com.idle.setting.worker

import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.usecase.auth.LogoutWorkerUseCase
import com.idle.setting.R
import com.idle.setting.SettingEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerSettingViewModel @Inject constructor(
    private val logoutWorkerUseCase: LogoutWorkerUseCase,
) : BaseViewModel() {
    private val _workerSettingEvent = MutableSharedFlow<SettingEvent>()
    val workerSettingEvent = _workerSettingEvent.asSharedFlow()

    fun logout() = viewModelScope.launch {
        logoutWorkerUseCase().onSuccess {
            workerSettingEvent(SettingEvent.LogoutSuccess)
        }.onFailure { }
    }

    fun clickLogout() = workerSettingEvent(SettingEvent.Logout)
    fun clickWithdrawal() = workerSettingEvent(SettingEvent.Withdrawal)
    fun clickProfile() = workerSettingEvent(SettingEvent.Profile)
    fun clickFAQ() = workerSettingEvent(SettingEvent.FAQ)
    fun clickTermsAndPolicies() = workerSettingEvent(SettingEvent.TermsAndPolicies)
    fun clickPrivacyAndPolicy() = workerSettingEvent(SettingEvent.PrivacyPolicy)

    private fun workerSettingEvent(event: SettingEvent) = viewModelScope.launch {
        _workerSettingEvent.emit(event)
    }
}