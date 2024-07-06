package com.idle.signin.center.newpassword

import androidx.lifecycle.ViewModel
import com.idle.common_ui.DeepLinkDestination
import com.idle.signin.center.newpassword.GenerateNewPasswordProcess.PHONE_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NewPasswordViewModel @Inject constructor() : ViewModel() {
    private val _eventFlow = MutableSharedFlow<NewPasswordEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _centerPhoneNumber = MutableStateFlow("")
    val phoneNumber = _centerPhoneNumber.asStateFlow()

    private val _centerCertificateNumber = MutableStateFlow("")
    val certificationNumber = _centerCertificateNumber.asStateFlow()

    private val _generateNewPasswordProcess = MutableStateFlow(PHONE_NUMBER)
    val generateNewPasswordProcess = _generateNewPasswordProcess.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword = _newPassword.asStateFlow()

    private val _newPasswordForConfirm = MutableStateFlow("")
    val newPasswordForConfirm = _newPasswordForConfirm.asStateFlow()

    internal fun setPhoneNumber(phoneNumber: String) {
        _centerPhoneNumber.value = phoneNumber
    }

    internal fun setCertificationNumber(certificateNumber: String) {
        _centerCertificateNumber.value = certificateNumber
    }

    internal fun setGenerateNewPasswordProcess(process: GenerateNewPasswordProcess) {
        _generateNewPasswordProcess.value = process
    }

    internal fun setNewPassword(password: String) {
        _newPassword.value = password
    }

    internal fun setNewPasswordForConfirm(passwordForConfirm: String) {
        _newPasswordForConfirm.value = passwordForConfirm
    }
}

sealed class NewPasswordEvent {
    data class NavigateTo(val destination: DeepLinkDestination) : NewPasswordEvent()
}

enum class GenerateNewPasswordProcess {
    PHONE_NUMBER, GENERATE_NEW_PASSWORD
}