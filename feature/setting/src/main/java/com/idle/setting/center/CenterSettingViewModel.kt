package com.idle.setting.center

import androidx.lifecycle.viewModelScope
import com.idle.analytics.helper.AnalyticsHelper
import com.idle.binding.NavigationEvent
import com.idle.binding.NavigationHelper
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.error.ErrorHandlerHelper
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
    private val analyticsHelper: AnalyticsHelper,
    private val errorHandlerHelper: ErrorHandlerHelper,
    val navigationHelper: NavigationHelper,
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
        }.onFailure { errorHandlerHelper.sendError(it) }
    }

    fun logout() = viewModelScope.launch {
        logoutCenterUseCase().onSuccess {
            analyticsHelper.setUserId(null)
            navigationHelper.navigateTo(
                NavigationEvent.NavigateToAuthWithClearBackStack("로그아웃이 완료되었습니다.|SUCCESS")
            )
        }.onFailure { errorHandlerHelper.sendError(it) }
    }

    fun clickLogout() = centerSettingEvent(SettingEvent.Logout)
    fun clickWithdrawal() = centerSettingEvent(SettingEvent.Withdrawal)
    fun clickCenterProfile() = centerSettingEvent(SettingEvent.Profile)
    fun clickFAQ() = centerSettingEvent(SettingEvent.FAQ)
    fun clickInquiry() = centerSettingEvent(SettingEvent.Inquiry)
    fun clickTermsAndPolicies() = centerSettingEvent(SettingEvent.TermsAndPolicies)
    fun clickPrivacyAndPolicy() = centerSettingEvent(SettingEvent.PrivacyPolicy)

    private fun centerSettingEvent(event: SettingEvent) = viewModelScope.launch {
        _centerSettingEvent.emit(event)
    }
}