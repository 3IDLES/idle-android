package com.idle.signin.center

import androidx.lifecycle.viewModelScope
import com.idle.analytics.AnalyticsEvent
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.ACTION_NAME
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.ACTION_RESULT
import com.idle.analytics.helper.AnalyticsHelper
import com.idle.binding.DeepLinkDestination.CenterHome
import com.idle.binding.DeepLinkDestination.CenterPending
import com.idle.binding.DeepLinkDestination.CenterRegister
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.domain.model.error.ApiErrorCode
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.profile.CenterManagerAccountStatus
import com.idle.domain.usecase.auth.SignInCenterUseCase
import com.idle.domain.usecase.profile.GetCenterStatusUseCase
import com.idle.domain.usecase.profile.GetMyCenterProfileUseCase
import com.idle.signin.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterSignInViewModel @Inject constructor(
    private val signInCenterUseCase: SignInCenterUseCase,
    private val getCenterStatusUseCase: GetCenterStatusUseCase,
    private val getMyCenterProfileUseCase: GetMyCenterProfileUseCase,
    private val analyticsHelper: AnalyticsHelper,
) : BaseViewModel() {
    private val _centerId = MutableStateFlow("")
    internal val centerId = _centerId.asStateFlow()

    private val _centerPassword = MutableStateFlow("")
    internal val centerPassword = _centerPassword.asStateFlow()

    internal fun setCenterId(id: String) {
        _centerId.value = id
    }

    internal fun setCenterPassword(password: String) {
        _centerPassword.value = password
    }

    internal fun signInCenter() = viewModelScope.launch {
        signInCenterUseCase(identifier = _centerId.value, password = _centerPassword.value)
            .onSuccess {
                analyticsHelper.setUserId(_centerId.value)
                handleCenterLoginSuccess()
            }
            .onFailure { exception ->
                handleLoginFailure(exception as HttpResponseException)
            }
    }

    private fun handleCenterLoginSuccess() = viewModelScope.launch {
        getCenterStatusUseCase().onSuccess { centerStatusResponse ->
            navigateBasedOnCenterStatus(centerStatusResponse.centerManagerAccountStatus)
        }.onFailure { exception ->
            handleFailure(exception as HttpResponseException)
        }
    }

    private fun navigateBasedOnCenterStatus(status: CenterManagerAccountStatus) {
        when (status) {
            CenterManagerAccountStatus.APPROVED -> fetchAndNavigateToProfile()
            else -> baseEvent(NavigateTo(CenterPending(status.name), R.id.centerSignInFragment))
        }
    }

    private fun fetchAndNavigateToProfile() = viewModelScope.launch {
        getMyCenterProfileUseCase().onSuccess {
            baseEvent(NavigateTo(CenterHome, R.id.centerSignInFragment))
        }.onFailure {
            val error = it as HttpResponseException
            if (error.apiErrorCode == ApiErrorCode.CenterNotFound) {
                baseEvent(NavigateTo(CenterRegister, R.id.centerSignInFragment))
            } else {
                handleFailure(error)
            }
        }
    }

    private fun handleLoginFailure(error: HttpResponseException) {
        handleFailure(error)
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