package com.idle.signin.center

import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination.CenterHome
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.domain.usecase.th.SignInCenterUseCase
import com.idle.signin.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterSignInViewModel @Inject constructor(
    private val signInCenterUseCase: SignInCenterUseCase,
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
            .onSuccess { baseEvent(NavigateTo(CenterHome, R.id.centerSignInFragment)) }
            .onFailure { baseEvent(CareBaseEvent.ShowSnackBar(it.message.toString())) }
    }
}