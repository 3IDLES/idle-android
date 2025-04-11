package com.idle.pending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.binding.EventHelper
import com.idle.binding.MainEvent
import com.idle.binding.ToastType.SUCCESS
import com.idle.center.pending.R
import com.idle.domain.model.error.ApiErrorCode
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.profile.CenterManagerAccountStatus
import com.idle.domain.repositorry.AuthRepository
import com.idle.domain.repositorry.ProfileRepository
import com.idle.navigation.DeepLinkDestination.CenterHome
import com.idle.navigation.DeepLinkDestination.CenterRegister
import com.idle.navigation.NavigationEvent
import com.idle.navigation.NavigationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterPendingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val errorHelper: ErrorHelper,
    private val eventHelper: EventHelper,
    private val navigationHelper: NavigationHelper,
) : ViewModel() {
    private val _status = MutableStateFlow(CenterManagerAccountStatus.UNKNOWN)
    val status = _status.asStateFlow()

    private var pollingJob = MutableStateFlow(false)

    init {
        subscribeCenterStatus()
    }

    internal fun setStatus(status: String) {
        _status.value = CenterManagerAccountStatus.create(status)
    }

    internal fun logout() = viewModelScope.launch {
        authRepository.logoutCenter().onSuccess {
            navigationHelper.navigateTo(
                NavigationEvent.NavigateToAuthWithClearBackStack(
                    toastMsg = "로그아웃이 완료되었습니다.",
                    toastType = "SUCCESS"
                )
            )
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun sendVerificationRequest() = viewModelScope.launch {
        authRepository.sendCenterVerificationRequest().onSuccess {
            _status.value = CenterManagerAccountStatus.PENDING
            eventHelper.sendEvent(MainEvent.ShowToast("센터 인증 요청이 완료되었습니다.", SUCCESS))
        }.onFailure { errorHelper.sendError(it) }
    }

    private fun subscribeCenterStatus() {
        viewModelScope.launch {
            pollingJob.emit(true)
            while (isActive && pollingJob.value) {
                getCenterStatus()
                delay(2000)
            }
        }
    }

    private fun getCenterStatus() = viewModelScope.launch {
        profileRepository.getCenterStatus().onSuccess {
            when (it.centerManagerAccountStatus) {
                CenterManagerAccountStatus.APPROVED -> {
                    handleApprovedCenterStatus()
                    eventHelper.sendEvent(MainEvent.ShowToast("센터 인증이 완료되었습니다.", SUCCESS))
                }

                else -> Unit
            }
        }.onFailure { errorHelper.sendError(it) }
    }

    private fun handleApprovedCenterStatus() = viewModelScope.launch {
        profileRepository.getMyCenterProfile().onSuccess {
            navigationHelper.navigateTo(
                NavigationEvent.To(CenterHome, R.id.centerPendingFragment)
            )
        }.onFailure {
            val error = it as HttpResponseException
            if (error.apiErrorCode == ApiErrorCode.CenterNotFound) {
                navigationHelper.navigateTo(
                    NavigationEvent.To(
                        CenterRegister,
                        R.id.centerPendingFragment,
                    )
                )
            }
        }
    }
}
