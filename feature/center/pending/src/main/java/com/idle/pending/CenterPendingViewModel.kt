package com.idle.pending

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.usecase.auth.LogoutCenterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterPendingViewModel @Inject constructor(
    private val logoutCenterUseCase: LogoutCenterUseCase,
) : BaseViewModel() {
    internal fun logout() = viewModelScope.launch {
        logoutCenterUseCase().onSuccess {
            baseEvent(CareBaseEvent.NavigateToAuthWithClearBackStack("로그아웃이 완료되었습니다.|SUCCESS"))
        }.onFailure { handleFailure(it as HttpResponseException) }
    }
}