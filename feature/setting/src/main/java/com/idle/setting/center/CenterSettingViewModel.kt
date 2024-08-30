package com.idle.setting.center

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.usecase.auth.LogoutCenterUseCase
import com.idle.domain.usecase.profile.GetLocalMyCenterProfileUseCase
import com.idle.setting.SettingEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterSettingViewModel @Inject constructor(
    private val getLocalMyCenterProfileUseCase: GetLocalMyCenterProfileUseCase,
    private val logoutCenterUseCase: LogoutCenterUseCase,
) : BaseViewModel() {
    private val _centerProfile =
        MutableStateFlow<CenterProfile>(CenterProfile("", "", "", "", "", 0.0, 0.0, "", ""))
    val centerProfile = _centerProfile.asStateFlow()

    private val _centerSettingEvent = MutableSharedFlow<SettingEvent>()
    val centerSettingEvent = _centerSettingEvent.asSharedFlow()

    init {
        getMyProfile()
    }

    private fun getMyProfile() = viewModelScope.launch {
        getLocalMyCenterProfileUseCase().onSuccess {
            _centerProfile.value = it
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    fun logout() = viewModelScope.launch {
        logoutCenterUseCase().onSuccess {
            baseEvent(CareBaseEvent.NavigateToAuthWithClearBackStack)
        }.onFailure { handleFailure(it as HttpResponseException) }
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