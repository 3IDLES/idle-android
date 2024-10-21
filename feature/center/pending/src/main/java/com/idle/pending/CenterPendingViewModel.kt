package com.idle.pending

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.EventHandler
import com.idle.binding.base.MainEvent
import com.idle.binding.base.SnackBarType.SUCCESS
import com.idle.domain.model.error.ErrorHandler
import com.idle.domain.model.profile.CenterManagerAccountStatus
import com.idle.domain.usecase.auth.LogoutCenterUseCase
import com.idle.domain.usecase.auth.SendCenterVerificationRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterPendingViewModel @Inject constructor(
    private val logoutCenterUseCase: LogoutCenterUseCase,
    private val sendCenterVerificationRequestUseCase: SendCenterVerificationRequestUseCase,
    private val errorHandler: ErrorHandler,
    private val eventHandler: EventHandler,
) : BaseViewModel() {
    private val _status = MutableStateFlow(CenterManagerAccountStatus.UNKNOWN)
    val status = _status.asStateFlow()

    internal fun setStatus(status: String) {
        _status.value = CenterManagerAccountStatus.create(status)
    }

    internal fun logout() = viewModelScope.launch {
        logoutCenterUseCase().onSuccess {
            eventHandler.sendEvent(MainEvent.NavigateToAuthWithClearBackStack("로그아웃이 완료되었습니다.|SUCCESS"))
        }.onFailure { errorHandler.sendError(it) }
    }

    internal fun sendVerificationRequest() = viewModelScope.launch {
        sendCenterVerificationRequestUseCase().onSuccess {
            _status.value = CenterManagerAccountStatus.PENDING
            eventHandler.sendEvent(MainEvent.ShowSnackBar("센터 인증 요청이 완료되었습니다.", SUCCESS))
        }.onFailure { errorHandler.sendError(it) }
    }
}