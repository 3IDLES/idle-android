package com.idle.signin.center

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.domain.usecase.auth.SignInCenterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterSignInViewModel @Inject constructor(
    private val signInCenterUseCase: SignInCenterUseCase,
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<CenterSignInEvent>()
    internal val eventFlow = _eventFlow.asSharedFlow()

    private val _centerId = MutableStateFlow("")
    internal val centerId = _centerId.asStateFlow()

    private val _centerPassword = MutableStateFlow("")
    internal val centerPassword = _centerPassword.asStateFlow()

    internal fun event(event: CenterSignInEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }

    internal fun setCenterId(id: String) {
        _centerId.value = id
    }

    internal fun setCenterPassword(password: String) {
        _centerPassword.value = password
    }

    internal fun signInCenter() = viewModelScope.launch {
        signInCenterUseCase(identifier = _centerId.value, password = _centerPassword.value)
            .onSuccess { }
            .onFailure { }
    }
}

sealed class CenterSignInEvent {
    data class NavigateTo(val destination: com.idle.binding.DeepLinkDestination) :
        CenterSignInEvent()
}