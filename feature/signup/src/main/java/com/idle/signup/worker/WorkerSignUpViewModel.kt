package com.idle.signin.worker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import com.idle.domain.usecase.auth.ConfirmAuthCodeUseCase
import com.idle.domain.usecase.auth.SendPhoneNumberUseCase
import com.idle.signin.worker.WorkerSignUpProcess.NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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

    private val _signUpProcess = MutableStateFlow<WorkerSignUpProcess>(NAME)
    internal val signUpProcess = _signUpProcess.asStateFlow()

    private val _workerName = MutableStateFlow("")
    internal val workerName = _workerName.asStateFlow()

    private val _workerPhoneNumber = MutableStateFlow("")
    internal val workerPhoneNumber = _workerPhoneNumber.asStateFlow()

    private val _workerAuthCode = MutableStateFlow("")
    internal val workerAuthCode = _workerAuthCode.asStateFlow()

    private val _gender = MutableStateFlow(Gender.NONE)
    internal val gender = _gender.asStateFlow()

    private val _address = MutableStateFlow("")
    internal val address = _address.asStateFlow()

    private val _addressDetail = MutableStateFlow("")
    internal val addressDetail = _addressDetail.asStateFlow()

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

    internal fun setWorkerAuthCode(certificateNumber: String) {
        _workerAuthCode.value = certificateNumber
    }

    internal fun setGender(gender: Gender) {
        _gender.value = gender
    }

    internal fun setAddress(address: String) {
        _address.value = address
    }

    internal fun setAddressDetail(addressDetail: String) {
        _addressDetail.value = addressDetail
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

enum class WorkerSignUpProcess(val step: Int) {
    NAME(1), GENDER(2), PHONE_NUMBER(3), ADDRESS(4)
}

enum class Gender(val displayName: String) {
    MALE("남성"), FEMALE("여성"), NONE("")
}