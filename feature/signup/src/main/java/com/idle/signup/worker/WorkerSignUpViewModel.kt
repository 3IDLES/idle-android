package com.idle.signin.worker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.common_ui.DeepLinkDestination
import com.idle.signin.worker.WorkerSignUpProcess.PHONE_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerSignUpViewModel @Inject constructor() : ViewModel() {
    private val _eventFlow = MutableSharedFlow<WorkerSignUpEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _signUpProcess = MutableStateFlow<WorkerSignUpProcess>(PHONE_NUMBER)
    val signUpProcess = _signUpProcess.asStateFlow()

    private val _workerName = MutableStateFlow("")
    val workerName = _workerName.asStateFlow()

    private val _workerPhoneNumber = MutableStateFlow("")
    val workerPhoneNumber = _workerPhoneNumber.asStateFlow()

    private val _workerCertificateNumber = MutableStateFlow("")
    val workerCertificateNumber = _workerCertificateNumber.asStateFlow()

    private val _gender = MutableStateFlow(Gender.NONE)
    val gender = _gender.asStateFlow()

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
        _workerCertificateNumber.value = certificateNumber
    }

    internal fun setGender(gender: Gender) {
        _gender.value = gender
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