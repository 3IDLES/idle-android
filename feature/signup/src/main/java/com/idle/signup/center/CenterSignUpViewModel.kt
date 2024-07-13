package com.idle.signin.center

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.domain.usecase.auth.ConfirmAuthCodeUseCase
import com.idle.domain.usecase.auth.SendPhoneNumberUseCase
import com.idle.domain.usecase.auth.SignUpCenterUseCase
import com.idle.domain.usecase.auth.ValidateIdentifierUseCase
import com.idle.signin.center.CenterSignUpProcess.NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterSignUpViewModel @Inject constructor(
    private val sendPhoneNumberUseCase: SendPhoneNumberUseCase,
    private val confirmAuthCodeUseCase: ConfirmAuthCodeUseCase,
    private val signUpCenterUseCase: SignUpCenterUseCase,
    private val validateIdentifierUseCase: ValidateIdentifierUseCase,
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<CenterSignUpEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _signUpProcess = MutableStateFlow<CenterSignUpProcess>(NAME)
    val signUpProcess = _signUpProcess.asStateFlow()

    private val _centerName = MutableStateFlow("")
    val centerName = _centerName.asStateFlow()

    private val _centerPhoneNumber = MutableStateFlow("")
    val centerPhoneNumber = _centerPhoneNumber.asStateFlow()

    private val _centerAuthCode = MutableStateFlow("")
    val centerConfirmNumber = _centerAuthCode.asStateFlow()

    private val _businessRegistrationNumber = MutableStateFlow("")
    val businessRegistrationNumber = _businessRegistrationNumber.asStateFlow()

    private val _centerId = MutableStateFlow("")
    val centerId = _centerId.asStateFlow()

    private val _centerPassword = MutableStateFlow("")
    val centerPassword = _centerPassword.asStateFlow()

    private val _centerPasswordForConfirm = MutableStateFlow("")
    val centerPasswordForConfirm = _centerPasswordForConfirm.asStateFlow()

    internal fun event(event: CenterSignUpEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }

    internal fun setCenterSignUpProcess(process: CenterSignUpProcess) {
        _signUpProcess.value = process
    }

    internal fun setCenterName(name: String) {
        _centerName.value = name
    }

    internal fun setCenterPhoneNumber(phoneNumber: String) {
        _centerPhoneNumber.value = phoneNumber
    }

    internal fun setCenterAuthCode(authCode: String) {
        _centerAuthCode.value = authCode
    }

    internal fun setBusinessRegistrationNumber(businessRegistrationNumber: String) {
        _businessRegistrationNumber.value = businessRegistrationNumber
    }

    internal fun setCenterId(id: String) {
        _centerId.value = id
    }

    internal fun setCenterPassword(password: String) {
        _centerPassword.value = password
    }

    internal fun setCenterPasswordForConfirm(passwordForConfirm: String) {
        _centerPasswordForConfirm.value = passwordForConfirm
    }

    internal fun sendPhoneNumber() = viewModelScope.launch {
        sendPhoneNumberUseCase(_centerPhoneNumber.value)
            .onSuccess { Log.d("test", "성공!") }
            .onFailure { Log.d("test", "실패! ${it}") }
    }

    internal fun confirmAuthCode() = viewModelScope.launch {
        confirmAuthCodeUseCase(_centerPhoneNumber.value, _centerAuthCode.value)
            .onSuccess { Log.d("test", "성공!") }
            .onFailure { Log.d("test", "실패! ${it}") }
    }

    internal fun signUpCenter() = viewModelScope.launch {
        signUpCenterUseCase(
            identifier = _centerId.value,
            password = _centerPassword.value,
            phoneNumber = _centerPhoneNumber.value,
            managerName = _centerName.value,
            businessRegistrationNumber = _businessRegistrationNumber.value,
        )
            .onSuccess { Log.d("test", "성공!") }
            .onFailure { Log.d("test", "실패! ${it}") }
    }

    internal fun validateIdentifier() = viewModelScope.launch {
        validateIdentifierUseCase(identifier = _centerId.value)
            .onSuccess { Log.d("test", "성공!") }
            .onFailure { Log.d("test", it.toString()) }
    }
}

sealed class CenterSignUpEvent {
    data class NavigateTo(val destination: com.idle.binding.DeepLinkDestination) :
        CenterSignUpEvent()
}

enum class CenterSignUpProcess(val step: Int) {
    NAME(1), PHONE_NUMBER(2), BUSINESS_REGISTRAION_NUMBER(3), ID_PASSWORD(4)
}