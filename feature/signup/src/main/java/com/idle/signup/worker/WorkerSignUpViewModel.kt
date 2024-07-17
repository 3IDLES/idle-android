package com.idle.signin.worker

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.CountDownTimer
import com.idle.domain.model.CountDownTimer.Companion.SECONDS_PER_MINUTE
import com.idle.domain.model.CountDownTimer.Companion.TICK_INTERVAL
import com.idle.domain.usecase.auth.ConfirmAuthCodeUseCase
import com.idle.domain.usecase.auth.SendPhoneNumberUseCase
import com.idle.signin.worker.WorkerSignUpProcess.NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerSignUpViewModel @Inject constructor(
    private val sendPhoneNumberUseCase: SendPhoneNumberUseCase,
    private val confirmAuthCodeUseCase: ConfirmAuthCodeUseCase,
    private val countDownTimer: CountDownTimer,
) : BaseViewModel() {

    private val _signUpProcess = MutableStateFlow<WorkerSignUpProcess>(NAME)
    internal val signUpProcess = _signUpProcess.asStateFlow()

    private val _workerName = MutableStateFlow("")
    internal val workerName = _workerName.asStateFlow()

    private val _workerPhoneNumber = MutableStateFlow("")
    internal val workerPhoneNumber = _workerPhoneNumber.asStateFlow()

    private var timerJob: Job? = null

    private val _workerAuthCodeTimerMinute = MutableStateFlow("")
    val workerAuthCodeTimerMinute = _workerAuthCodeTimerMinute.asStateFlow()

    private val _workerAuthCodeTimerSeconds = MutableStateFlow("")
    val workerAuthCodeTimerSeconds = _workerAuthCodeTimerSeconds.asStateFlow()

    private val _workerAuthCode = MutableStateFlow("")
    internal val workerAuthCode = _workerAuthCode.asStateFlow()

    private val _isConfirmAuthCode = MutableStateFlow(false)
    val isConfirmAuthCode = _isConfirmAuthCode.asStateFlow()

    private val _gender = MutableStateFlow(Gender.NONE)
    internal val gender = _gender.asStateFlow()

    private val _address = MutableStateFlow("")
    internal val address = _address.asStateFlow()

    private val _addressDetail = MutableStateFlow("")
    internal val addressDetail = _addressDetail.asStateFlow()

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
            .onSuccess { startTimer() }
            .onFailure { Log.d("test", "실패! ${it}") }
    }

    private fun startTimer() {
        cancelTimer()

        timerJob = viewModelScope.launch {
            countDownTimer.start(limitTime = TICK_INTERVAL * SECONDS_PER_MINUTE * 5)
                .collect { timeMillis ->
                    updateTimerDisplay(timeMillis)
                }
        }
    }

    private fun updateTimerDisplay(timeMillis: Long) {
        val minutes =
            (timeMillis / (TICK_INTERVAL * SECONDS_PER_MINUTE)).toString().padStart(2, '0')
        val seconds =
            ((timeMillis % (TICK_INTERVAL * SECONDS_PER_MINUTE)) / TICK_INTERVAL).toString()
                .padStart(2, '0')

        _workerAuthCodeTimerMinute.value = minutes
        _workerAuthCodeTimerSeconds.value = seconds
    }

    private fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    internal fun confirmAuthCode() = viewModelScope.launch {
        confirmAuthCodeUseCase(_workerPhoneNumber.value, _workerAuthCode.value)
            .onSuccess {
                cancelTimer()
                _isConfirmAuthCode.value = true
            }
            .onFailure { Log.d("test", "실패! ${it}") }
    }
}

enum class WorkerSignUpProcess(val step: Int) {
    NAME(1), GENDER(2), PHONE_NUMBER(3), ADDRESS(4)
}

enum class Gender(val displayName: String) {
    MALE("남성"), FEMALE("여성"), NONE("")
}