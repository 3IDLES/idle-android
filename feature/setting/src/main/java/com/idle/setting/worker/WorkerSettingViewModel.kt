package com.idle.setting.worker

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.usecase.auth.LogoutWorkerUseCase
import com.idle.domain.usecase.profile.GetLocalMyWorkerProfileUseCase
import com.idle.setting.SettingEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerSettingViewModel @Inject constructor(
    private val getLocalMyWorkerProfileUseCase: GetLocalMyWorkerProfileUseCase,
    private val logoutWorkerUseCase: LogoutWorkerUseCase,
) : BaseViewModel() {
    private val _workerProfile = MutableStateFlow<WorkerProfile?>(null)
    val workerProfile = _workerProfile.asStateFlow()

    private val _workerSettingEvent = MutableSharedFlow<SettingEvent>()
    val workerSettingEvent = _workerSettingEvent.asSharedFlow()

    init {
        getMyProfile()
    }

    private fun getMyProfile() = viewModelScope.launch {
        getLocalMyWorkerProfileUseCase().onSuccess {
            _workerProfile.value = it
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    fun logout() = viewModelScope.launch {
        logoutWorkerUseCase().onSuccess {
            workerSettingEvent(SettingEvent.LogoutSuccess)
        }.onFailure { handleFailure(it as HttpResponseException) }
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