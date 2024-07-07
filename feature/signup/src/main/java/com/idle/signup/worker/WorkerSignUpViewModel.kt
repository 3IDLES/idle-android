package com.idle.signin.worker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.common_ui.DeepLinkDestination
import com.idle.domain.usecase.auth.ConfirmAuthCodeUseCase
import com.idle.domain.usecase.auth.SendPhoneNumberUseCase
import com.idle.signin.worker.WorkerSignUpProcess.PHONE_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerSignUpViewModel @Inject constructor(
    private val sendPhoneNumberUseCase: SendPhoneNumberUseCase,
    private val confirmAuthCodeUseCase: ConfirmAuthCodeUseCase,
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<WorkerSignUpEvent>()
    internal val eventFlow = _eventFlow.asSharedFlow()

    private val _signUpProcess = MutableStateFlow<WorkerSignUpProcess>(PHONE_NUMBER)
    internal val signUpProcess = _signUpProcess.asStateFlow()

    private val _workerName = MutableStateFlow("")
    internal val workerName = _workerName.asStateFlow()

    private val _workerPhoneNumber = MutableStateFlow("")
    internal val workerPhoneNumber = _workerPhoneNumber.asStateFlow()

    private val _workerAuthCode = MutableStateFlow("")
    internal val workerConfirmNumber = _workerAuthCode.asStateFlow()

    private val _gender = MutableStateFlow(Gender.NONE)
    internal val gender = _gender.asStateFlow()

    internal fun event(event: WorkerSignUpEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }

    internal fun setWorkerSignUpProcess(process: WorkerSignUpProcess) {
        _signUpProcess.value = process
    }

    internal fun setWorkerName(name: String) {
        _workerName.value = name
    }

    internal fun setWorkerPhoneNumber(phoneNumber: String) {
        _workerPhoneNumber.value = phoneNumber
    }

    internal fun setWorkerCertificateNumber(certificateNumber: String) {
        _workerAuthCode.value = certificateNumber
    }

    internal fun setGender(gender: Gender) {
        _gender.value = gender
    }

    internal fun sendPhoneNumber() = viewModelScope.launch {
        sendPhoneNumberUseCase(_workerPhoneNumber.value)
            .onSuccess { Log.d("test", "성공!") }
            .onFailure { Log.d("test", "실패! ${it}") }
    }

    internal fun confirmAuthCode() = viewModelScope.launch {
        confirmAuthCodeUseCase(_workerPhoneNumber.value, _workerAuthCode.value)
            .onSuccess { Log.d("test", "성공!") }
            .onFailure { Log.d("test", "실패! ${it}") }
    }
}

sealed class WorkerSignUpEvent {
    data class NavigateTo(val destination: DeepLinkDestination) : WorkerSignUpEvent()
}

enum class WorkerSignUpProcess {
    PHONE_NUMBER, NAME, GENDER
}

enum class Gender {
    MALE, FEMALE, NONE
}