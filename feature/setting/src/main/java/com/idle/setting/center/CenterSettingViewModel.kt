package com.idle.setting.center

import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.usecase.auth.LogoutCenterUseCase
import com.idle.setting.R
import com.idle.setting.SettingEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterSettingViewModel @Inject constructor(
    private val logoutCenterUseCase: LogoutCenterUseCase,
) : BaseViewModel() {
    private val _centerSettingEvent = MutableSharedFlow<SettingEvent>()
    val centerSettingEvent = _centerSettingEvent.asSharedFlow()

    fun logout() = viewModelScope.launch {
        logoutCenterUseCase().onSuccess {
            centerSettingEvent(SettingEvent.LogoutSuccess)
        }.onFailure { }
    }

    fun clickLogout() = centerSettingEvent(SettingEvent.Logout)
    fun clickWithdrawal() = centerSettingEvent(SettingEvent.Withdrawal)
    fun clickCenterProfile() = centerSettingEvent(SettingEvent.Profile)
    fun clickFAQ() = centerSettingEvent(SettingEvent.FAQ)
    fun clickTermsAndPolicies() = centerSettingEvent(SettingEvent.TermsAndPolicies)
    fun clickPrivacyAndPolicy() = centerSettingEvent(SettingEvent.PrivacyPolicy)

    private fun centerSettingEvent(event: SettingEvent) = viewModelScope.launch {
        _centerSettingEvent.emit(event)
    }
}