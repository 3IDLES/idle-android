package com.idle.signin.center

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.common_ui.DeepLinkDestination
import com.idle.signin.center.CenterSignUpProcess.NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterSignUpViewModel @Inject constructor() : ViewModel() {
    private val _eventFlow = MutableSharedFlow<CenterSignUpEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _signUpProcess = MutableStateFlow<CenterSignUpProcess>(NAME)
    val signUpProcess = _signUpProcess.asStateFlow()

    private val _centerName = MutableStateFlow("")
    val centerName = _centerName.asStateFlow()

    private val _centerPhoneNumber = MutableStateFlow("")
    val centerPhoneNumber = _centerPhoneNumber.asStateFlow()

    private val _centerCertificateNumber = MutableStateFlow("")
    val centerCertificateNumber = _centerCertificateNumber.asStateFlow()

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

    internal fun setCenterCertificateNumber(certificateNumber: String) {
        _centerCertificateNumber.value = certificateNumber
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
}

sealed class CenterSignUpEvent {
    data class NavigateTo(val destination: DeepLinkDestination) : CenterSignUpEvent()
}

enum class CenterSignUpProcess {
    NAME, PHONE_NUMBER, BUSINESS_REGISTRAION_NUMBER, ID_PASSWORD
}