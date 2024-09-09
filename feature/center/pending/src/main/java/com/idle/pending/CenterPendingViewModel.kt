package com.idle.pending

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.error.HttpResponseException
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
) : BaseViewModel() {
    private val _status = MutableStateFlow(CenterManagerAccountStatus.UNKNOWN)
    val status = _status.asStateFlow()

    internal fun setStatus(status: String) {
        _status.value = CenterManagerAccountStatus.create(status)
    }

    internal fun logout() = viewModelScope.launch {
        logoutCenterUseCase().onSuccess {
            baseEvent(CareBaseEvent.NavigateToAuthWithClearBackStack("로그아웃이 완료되었습니다.|SUCCESS"))
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    internal fun sendVerificationRequest() = viewModelScope.launch {
        sendCenterVerificationRequestUseCase().onSuccess {
            _status.value = CenterManagerAccountStatus.PENDING
            baseEvent(CareBaseEvent.ShowSnackBar("센터 인증 요청이 완료되었습니다.|SUCCESS"))
        }.onFailure {
            handleFailure(it as HttpResponseException)
        }
    }
}