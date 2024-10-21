package com.idle.signin.center.newpassword

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import com.idle.binding.EventHandler
import com.idle.binding.MainEvent
import com.idle.binding.NavigationEvent
import com.idle.binding.NavigationRouter
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.CountDownTimer
import com.idle.domain.model.CountDownTimer.Companion.SECONDS_PER_MINUTE
import com.idle.domain.model.CountDownTimer.Companion.TICK_INTERVAL
import com.idle.domain.model.error.ErrorHandler
import com.idle.domain.usecase.auth.ConfirmAuthCodeUseCase
import com.idle.domain.usecase.auth.GenerateNewPasswordUseCase
import com.idle.domain.usecase.auth.SendPhoneNumberUseCase
import com.idle.signin.R
import com.idle.signin.center.newpassword.NewPasswordStep.GENERATE_NEW_PASSWORD
import com.idle.signin.center.newpassword.NewPasswordStep.PHONE_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewPasswordViewModel @Inject constructor(
    private val sendPhoneNumberUseCase: SendPhoneNumberUseCase,
    private val confirmAuthCodeUseCase: ConfirmAuthCodeUseCase,
    private val generateNewPasswordUseCase: GenerateNewPasswordUseCase,
    private val countDownTimer: CountDownTimer,
    private val errorHandler: ErrorHandler,
    private val eventHandler: EventHandler,
    private val navigationRouter: NavigationRouter,
) : BaseViewModel() {
    private val _phoneNumber = MutableStateFlow("")
    internal val phoneNumber = _phoneNumber.asStateFlow()

    private val _authCode = MutableStateFlow("")
    internal val authCode = _authCode.asStateFlow()

    private var timerJob: Job? = null

    private val _timerMinute = MutableStateFlow("")
    val timerMinute = _timerMinute.asStateFlow()

    private val _timerSeconds = MutableStateFlow("")
    val timerSeconds = _timerSeconds.asStateFlow()

    private val _isConfirmAuthCode = MutableStateFlow(false)
    val isConfirmAuthCode = _isConfirmAuthCode.asStateFlow()

    private val _newPasswordProcess = MutableStateFlow(PHONE_NUMBER)
    internal val newPasswordProcess = _newPasswordProcess.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    internal val newPassword = _newPassword.asStateFlow()

    private val _newPasswordForConfirm = MutableStateFlow("")
    internal val newPasswordForConfirm = _newPasswordForConfirm.asStateFlow()

    internal fun setPhoneNumber(phoneNumber: String) {
        if (phoneNumber.isDigitsOnly() && phoneNumber.length <= 11) {
            _phoneNumber.value = phoneNumber
        }
    }

    internal fun setAuthCode(certificateNumber: String) {
        this._authCode.value = certificateNumber
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
        sendPhoneNumberUseCase(_phoneNumber.value)
            .onSuccess { startTimer() }
            .onFailure { eventHandler.sendEvent(MainEvent.ShowSnackBar(it.message.toString())) }
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

        _timerMinute.value = minutes
        _timerSeconds.value = seconds
    }

    private fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    internal fun confirmAuthCode() = viewModelScope.launch {
        confirmAuthCodeUseCase(
            _phoneNumber.value,
            this@NewPasswordViewModel._authCode.value
        ).onSuccess {
            cancelTimer()
            _isConfirmAuthCode.value = true
            _newPasswordProcess.value = GENERATE_NEW_PASSWORD
        }.onFailure { errorHandler.sendError(it) }
    }


    internal fun generateNewPassword() = viewModelScope.launch {
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#\$%^&*()_+=-]{8,20}$".toRegex()

        if (!_newPassword.value.matches(passwordPattern)) {
            eventHandler.sendEvent(MainEvent.ShowSnackBar("비밀번호가 형식에 맞지 않습니다."))
            return@launch
        }

        generateNewPasswordUseCase(
            newPassword = _newPassword.value,
            phoneNumber = _phoneNumber.value
        ).onSuccess {
            navigationRouter.navigateTo(
                NavigationEvent.NavigateTo(
                    destination = DeepLinkDestination.CenterSignIn("새 비밀번호를 발급하였습니다.|SUCCESS"),
                    popUpTo = R.id.newPasswordFragment,
                )
            )
        }.onFailure { errorHandler.sendError(it) }
    }
}

enum class NewPasswordStep {
    PHONE_NUMBER, GENERATE_NEW_PASSWORD
}