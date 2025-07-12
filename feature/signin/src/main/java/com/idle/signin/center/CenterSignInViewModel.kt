package com.idle.signin.center

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.analytics.AnalyticsEvent
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.ACTION_NAME
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.ACTION_RESULT
import com.idle.analytics.AnalyticsHelper
import com.idle.binding.EventHelper
import com.idle.common.suspendRunCatching
import com.idle.domain.model.error.ApiErrorCode
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.error.HttpResponseStatus
import com.idle.domain.model.profile.CenterManagerAccountStatus
import com.idle.domain.repositorry.AuthRepository
import com.idle.domain.repositorry.ProfileRepository
import com.idle.navigation.DeepLinkDestination.CenterHome
import com.idle.navigation.DeepLinkDestination.CenterPending
import com.idle.navigation.DeepLinkDestination.CenterRegister
import com.idle.navigation.NavigationEvent.To
import com.idle.signin.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterSignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val analyticsHelper: AnalyticsHelper,
    private val errorHelper: ErrorHelper,
    val eventHelper: EventHelper,
    val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
    private val _centerId = MutableStateFlow("")
    internal val centerId = _centerId.asStateFlow()

    private val _centerPassword = MutableStateFlow("")
    internal val centerPassword = _centerPassword.asStateFlow()

    private val _isLoginError = MutableStateFlow(false)
    val isLoginError = _isLoginError.asStateFlow()

    internal fun setCenterId(id: String) {
        if (id.length > 20) {
            return
        }

        _centerId.value = id
        _isLoginError.value = false
    }

    internal fun setCenterPassword(password: String) {
        if (password.length > 20) {
            return
        }

        _centerPassword.value = password
        _isLoginError.value = false
    }

    internal fun signInCenter() = viewModelScope.launch {
        suspendRunCatching {
            authRepository.signInCenter(
                identifier = _centerId.value,
                password = _centerPassword.value
            )
        }.onSuccess {
            analyticsHelper.setUserId(_centerId.value)
            handleCenterLoginSuccess()
        }.onFailure {
            if (it is HttpResponseException && it.status == HttpResponseStatus.Unauthorized) {
                _isLoginError.value = true
                return@launch
            }

            errorHelper.sendError(it)

            analyticsHelper.logEvent(
                AnalyticsEvent(
                    type = AnalyticsEvent.Types.ACTION,
                    properties = mutableMapOf(
                        ACTION_NAME to "center_login",
                        ACTION_RESULT to false,
                    )
                )
            )
        }
    }

    private fun handleCenterLoginSuccess() = viewModelScope.launch {
        suspendRunCatching {
            profileRepository.getCenterStatus()
        }.onSuccess { centerStatusResponse ->
            navigateBasedOnCenterStatus(centerStatusResponse.centerManagerAccountStatus)
        }.onFailure { errorHelper.sendError(it) }
    }

    private fun navigateBasedOnCenterStatus(status: CenterManagerAccountStatus) {
        when (status) {
            CenterManagerAccountStatus.APPROVED -> fetchAndNavigateToProfile()
            else -> navigationHelper.navigateTo(
                To(
                    CenterPending(status.name),
                    R.id.centerSignInFragment
                )
            )
        }
    }

    private fun fetchAndNavigateToProfile() = viewModelScope.launch {
        suspendRunCatching {
            profileRepository.getMyCenterProfile()
        }.onSuccess {
            navigationHelper.navigateTo(To(CenterHome, R.id.centerSignInFragment))
        }.onFailure {
            val error = it as HttpResponseException
            if (error.apiErrorCode == ApiErrorCode.CenterNotFound) {
                navigationHelper.navigateTo(
                    To(CenterRegister, R.id.centerSignInFragment)
                )
            } else {
                errorHelper.sendError(it)
            }
        }
    }
}
