package com.idle.setting.center

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.analytics.AnalyticsHelper
import com.idle.common.suspendRunCatching
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.repositorry.AuthRepository
import com.idle.domain.usecase.profile.GetMyCenterProfileUseCase
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
    private val getMyCenterProfileUseCase: GetMyCenterProfileUseCase,
    private val authRepository: AuthRepository,
    private val analyticsHelper: AnalyticsHelper,
    private val errorHelper: ErrorHelper,
    val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
    private val _centerProfile = MutableStateFlow<CenterProfile>(
        CenterProfile("", "", "", "", "", "", 0.0, 0.0, "", "")
    )
    val centerProfile = _centerProfile.asStateFlow()

    private val _centerSettingEvent = MutableSharedFlow<SettingEvent>()
    val centerSettingEvent = _centerSettingEvent.asSharedFlow()

    init {
        getMyProfile()
    }

    private fun getMyProfile() = viewModelScope.launch {
        suspendRunCatching {
            getMyCenterProfileUseCase()
        }.onSuccess {
            _centerProfile.value = it
        }.onFailure { errorHelper.sendError(it) }
    }

    fun logout() = viewModelScope.launch {
        suspendRunCatching {
            authRepository.logoutCenter()
        }.onSuccess {
            analyticsHelper.setUserId(null)
            navigationHelper.navigateTo(
                com.idle.navigation.NavigationEvent.ToAuthWithClearBackStack(
                    toastMsg = "로그아웃이 완료되었습니다.",
                    toastType = "SUCCESS"
                )
            )
        }.onFailure { errorHelper.sendError(it) }
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
