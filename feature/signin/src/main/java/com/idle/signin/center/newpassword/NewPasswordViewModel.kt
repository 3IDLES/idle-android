package com.idle.signin.center.newpassword

import androidx.lifecycle.ViewModel
import com.idle.binding.DeepLinkDestination
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
    internal val eventFlow = _eventFlow.asSharedFlow()

    private val _centerPhoneNumber = MutableStateFlow("")
    internal val phoneNumber = _centerPhoneNumber.asStateFlow()

    private val _centerCertificateNumber = MutableStateFlow("")
    internal val certificationNumber = _centerCertificateNumber.asStateFlow()

    private val _generateNewPasswordProcess = MutableStateFlow(PHONE_NUMBER)
    internal val generateNewPasswordProcess = _generateNewPasswordProcess.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    internal val newPassword = _newPassword.asStateFlow()

    private val _newPasswordForConfirm = MutableStateFlow("")
    internal val newPasswordForConfirm = _newPasswordForConfirm.asStateFlow()

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
    data class NavigateTo(val destination: com.idle.binding.DeepLinkDestination) : NewPasswordEvent()
}

enum class GenerateNewPasswordProcess {
    PHONE_NUMBER, GENERATE_NEW_PASSWORD
}