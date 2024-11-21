package com.idle.pending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.binding.EventHelper
import com.idle.binding.MainEvent
import com.idle.binding.ToastType.SUCCESS
import com.idle.center.pending.R
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.profile.CenterManagerAccountStatus
import com.idle.domain.usecase.auth.LogoutCenterUseCase
import com.idle.domain.usecase.auth.SendCenterVerificationRequestUseCase
import com.idle.domain.usecase.profile.GetCenterStatusUseCase
import com.idle.navigation.DeepLinkDestination
import com.idle.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterPendingViewModel @Inject constructor(
    private val logoutCenterUseCase: LogoutCenterUseCase,
    private val sendCenterVerificationRequestUseCase: SendCenterVerificationRequestUseCase,
    private val getCenterStatusUseCase: GetCenterStatusUseCase,
    private val errorHelper: ErrorHelper,
    private val eventHelper: EventHelper,
    private val navigationHelper: com.idle.navigation.NavigationHelper,
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
        logoutCenterUseCase().onSuccess {
            navigationHelper.navigateTo(
                NavigationEvent.NavigateToAuthWithClearBackStack(
                    toastMsg = "로그아웃이 완료되었습니다.",
                    toastType = "SUCCESS"
                )
            )
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun sendVerificationRequest() = viewModelScope.launch {
        sendCenterVerificationRequestUseCase().onSuccess {
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
        getCenterStatusUseCase().onSuccess {
            when (it.centerManagerAccountStatus) {
                CenterManagerAccountStatus.APPROVED -> {
                    pollingJob.emit(false)
                    navigationHelper.navigateTo(
                        NavigationEvent.NavigateTo(
                            destination = DeepLinkDestination.CenterHome,
                            popUpTo = R.id.centerPendingFragment,
                        )
                    )

                    eventHelper.sendEvent(MainEvent.ShowToast("센터 인증이 완료되었습니다.", SUCCESS))
                }

                else -> Unit
            }
        }.onFailure { errorHelper.sendError(it) }
    }
}