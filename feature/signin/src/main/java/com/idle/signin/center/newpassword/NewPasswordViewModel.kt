package com.idle.signin.center.newpassword

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.usecase.auth.ConfirmAuthCodeUseCase
import com.idle.domain.usecase.auth.SendPhoneNumberUseCase
import com.idle.signin.center.newpassword.NewPasswordStep.PHONE_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewPasswordViewModel @Inject constructor(
    private val sendPhoneNumberUseCase: SendPhoneNumberUseCase,
    private val confirmAuthCodeUseCase: ConfirmAuthCodeUseCase,
) : BaseViewModel() {
    private val _centerPhoneNumber = MutableStateFlow("")
    internal val phoneNumber = _centerPhoneNumber.asStateFlow()

    private val _centerAuthCode = MutableStateFlow("")
    internal val centerAuthCode = _centerAuthCode.asStateFlow()

    private val _newPasswordProcess = MutableStateFlow(PHONE_NUMBER)
    internal val newPasswordProcess = _newPasswordProcess.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    internal val newPassword = _newPassword.asStateFlow()

    private val _newPasswordForConfirm = MutableStateFlow("")
    internal val newPasswordForConfirm = _newPasswordForConfirm.asStateFlow()

    internal fun setPhoneNumber(phoneNumber: String) {
        _centerPhoneNumber.value = phoneNumber
    }

    internal fun setAuthCode(certificateNumber: String) {
        _centerAuthCode.value = certificateNumber
    }

    internal fun setNewPasswordProcess(process: NewPasswordStep) {
        _newPasswordProcess.value = process
    }

    internal fun setNewPassword(password: String) {
        _newPassword.value = password
    }

    internal fun setNewPasswordForConfirm(passwordForConfirm: String) {
        _newPasswordForConfirm.value = passwordForConfirm
    }

    internal fun sendPhoneNumber() = viewModelScope.launch {
        sendPhoneNumberUseCase(_centerPhoneNumber.value)
            .onSuccess { Log.d("test", "성공!") }
            .onFailure {  baseEvent(CareBaseEvent.ShowSnackBar(it.message.toString()))}
    }

    internal fun confirmAuthCode() = viewModelScope.launch {
        confirmAuthCodeUseCase(_centerPhoneNumber.value, _centerAuthCode.value)
            .onSuccess { Log.d("test", "성공!") }
            .onFailure {  baseEvent(CareBaseEvent.ShowSnackBar(it.message.toString())) }
    }
}

enum class NewPasswordStep {
    PHONE_NUMBER, GENERATE_NEW_PASSWORD
}