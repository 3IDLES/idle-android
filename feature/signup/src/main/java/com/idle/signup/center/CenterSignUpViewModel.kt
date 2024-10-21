package com.idle.signup.center

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import com.idle.binding.EventHandlerHelper
import com.idle.binding.MainEvent
import com.idle.binding.NavigationEvent
import com.idle.binding.NavigationHelper
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.CountDownTimer
import com.idle.domain.model.CountDownTimer.Companion.SECONDS_PER_MINUTE
import com.idle.domain.model.CountDownTimer.Companion.TICK_INTERVAL
import com.idle.domain.model.auth.BusinessRegistrationInfo
import com.idle.domain.model.error.ErrorHandlerHelper
import com.idle.domain.usecase.auth.ConfirmAuthCodeUseCase
import com.idle.domain.usecase.auth.SendPhoneNumberUseCase
import com.idle.domain.usecase.auth.SignUpCenterUseCase
import com.idle.domain.usecase.auth.ValidateBusinessRegistrationNumberUseCase
import com.idle.domain.usecase.auth.ValidateIdentifierUseCase
import com.idle.signup.R
import com.idle.signup.center.CenterSignUpStep.NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterSignUpViewModel @Inject constructor(
    private val sendPhoneNumberUseCase: SendPhoneNumberUseCase,
    private val confirmAuthCodeUseCase: ConfirmAuthCodeUseCase,
    private val signUpCenterUseCase: SignUpCenterUseCase,
    private val validateIdentifierUseCase: ValidateIdentifierUseCase,
    private val validateBusinessRegistrationNumberUseCase: ValidateBusinessRegistrationNumberUseCase,
    private val countDownTimer: CountDownTimer,
    private val errorHandlerHelper: ErrorHandlerHelper,
    private val eventHandlerHelper: EventHandlerHelper,
    val navigationHelper: NavigationHelper,
) : BaseViewModel() {
    private val _signUpStep = MutableStateFlow(NAME)
    val signUpStep = _signUpStep.asStateFlow()

    private val _centerName = MutableStateFlow("")
    val centerName = _centerName.asStateFlow()

    private val _centerPhoneNumber = MutableStateFlow("")
    val centerPhoneNumber = _centerPhoneNumber.asStateFlow()

    private val _centerAuthCodeTimerMinute = MutableStateFlow("")
    val centerAuthCodeTimerMinute = _centerAuthCodeTimerMinute.asStateFlow()

    private val _centerAuthCodeTimerSeconds = MutableStateFlow("")
    val centerAuthCodeTimerSeconds = _centerAuthCodeTimerSeconds.asStateFlow()

    private var timerJob: Job? = null

    private val _centerAuthCode = MutableStateFlow("")
    val centerAuthCode = _centerAuthCode.asStateFlow()

    private val _isConfirmAuthCode = MutableStateFlow(false)
    val isConfirmAuthCode = _isConfirmAuthCode.asStateFlow()

    private val _businessRegistrationNumber = MutableStateFlow("")
    val businessRegistrationNumber = _businessRegistrationNumber.asStateFlow()

    private val _businessRegistrationInfo: MutableStateFlow<BusinessRegistrationInfo?> =
        MutableStateFlow(null)
    val businessRegistrationInfo = _businessRegistrationInfo.asStateFlow()

    private val _centerId = MutableStateFlow("")
    val centerId = _centerId.asStateFlow()

    private val _centerIdResult = MutableStateFlow(false)
    val centerIdResult = _centerIdResult.asStateFlow()

    private val _centerPassword = MutableStateFlow("")
    val centerPassword = _centerPassword.asStateFlow()

    private val _centerPasswordForConfirm = MutableStateFlow("")
    val centerPasswordForConfirm = _centerPasswordForConfirm.asStateFlow()

    private val _isValidId = MutableStateFlow(false)
    val isValidId = _isValidId.asStateFlow()

    private val _isValidPassword = MutableStateFlow(false)
    val isValidPassword = _isValidPassword.asStateFlow()

    internal fun setCenterSignUpStep(step: CenterSignUpStep) {
        _signUpStep.value = step
    }

    internal fun setCenterName(name: String) {
        _centerName.value = name
    }

    internal fun setCenterPhoneNumber(phoneNumber: String) {
        if (phoneNumber.isDigitsOnly() && phoneNumber.length <= 11) {
            _centerPhoneNumber.value = phoneNumber
        }
    }

    internal fun setCenterAuthCode(authCode: String) {
        _centerAuthCode.value = authCode
    }

    internal fun setBusinessRegistrationNumber(businessRegistrationNumber: String) {
        if (businessRegistrationNumber.isDigitsOnly() && businessRegistrationNumber.length <= 10) {
            _businessRegistrationNumber.value = businessRegistrationNumber
            _businessRegistrationInfo.value = null
        }
    }

    internal fun setCenterId(id: String) {
        _centerId.value = id
        _centerIdResult.value = false
        validateId(id)
    }

    internal fun setCenterPassword(password: String) {
        _centerPassword.value = password
        validatePassword(password)
    }

    internal fun setCenterPasswordForConfirm(passwordForConfirm: String) {
        _centerPasswordForConfirm.value = passwordForConfirm
    }

    internal fun sendPhoneNumber() = viewModelScope.launch {
        sendPhoneNumberUseCase(_centerPhoneNumber.value)
            .onSuccess { startTimer() }
            .onFailure { errorHandlerHelper.sendError(it) }
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

        _centerAuthCodeTimerMinute.value = minutes
        _centerAuthCodeTimerSeconds.value = seconds
    }

    private fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    internal fun confirmAuthCode() = viewModelScope.launch {
        confirmAuthCodeUseCase(_centerPhoneNumber.value, _centerAuthCode.value)
            .onSuccess {
                cancelTimer()
                _isConfirmAuthCode.value = true

                _signUpStep.value = CenterSignUpStep.BUSINESS_REGISTRATION
            }
            .onFailure { errorHandlerHelper.sendError(it) }
    }

    private fun validateId(id: String) {
        if (id.length !in 6..20) {
            _isValidId.value = false
            return
        }

        if (!id.matches(Regex("^[A-Za-z0-9]+$"))) {
            _isValidId.value = false
            return
        }

        _isValidId.value = true
    }

    private fun validatePassword(password: String) {
        if (password.length !in 8..20) {
            _isValidPassword.value = false
            return
        }

        if (!password.matches(Regex("^[A-Za-z0-9!@#\$%^&*()_+=-]+$"))) {
            _isValidPassword.value = false
            return
        }

        if (!password.matches(Regex(".*[A-Za-z].*")) || !password.matches(Regex(".*\\d.*"))) {
            _isValidPassword.value = false
            return
        }

        if (password.contains(" ")) {
            _isValidPassword.value = false
            return
        }

        if (containsSequentialCharacters(password)) {
            _isValidPassword.value = false
            return
        }

        _isValidPassword.value = true
    }

    private fun containsSequentialCharacters(password: String): Boolean {
        val limit = 3
        var maxSequence = 1

        for (i in 1 until password.length) {
            if (password[i] == password[i - 1]) {
                maxSequence += 1
                if (maxSequence >= limit) return true
            } else {
                maxSequence = 1
            }
        }
        return false
    }

    internal fun signUpCenter() = viewModelScope.launch {
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#\$%^&*()_+=-]{8,20}$".toRegex()

        if (!_centerPassword.value.matches(passwordPattern)) {
            eventHandlerHelper.sendEvent(MainEvent.ShowSnackBar("비밀번호가 형식에 맞지 않습니다."))
            return@launch
        }

        signUpCenterUseCase(
            identifier = _centerId.value,
            password = _centerPassword.value,
            phoneNumber = _centerPhoneNumber.value,
            managerName = _centerName.value,
            businessRegistrationNumber = _businessRegistrationNumber.value,
        ).onSuccess {
            navigationHelper.navigateTo(
                NavigationEvent.NavigateTo(
                    DeepLinkDestination.CenterSignIn("회원가입을 성공하였습니다.|SUCCESS"),
                    R.id.centerSignUpFragment
                )
            )
        }
            .onFailure { errorHandlerHelper.sendError(it) }
    }

    internal fun validateIdentifier() = viewModelScope.launch {
        validateIdentifierUseCase(_centerId.value)
            .onSuccess { _centerIdResult.value = true }
            .onFailure { errorHandlerHelper.sendError(it) }
    }

    internal fun validateBusinessRegistrationNumber() = viewModelScope.launch {
        validateBusinessRegistrationNumberUseCase(_businessRegistrationNumber.value)
            .onSuccess { _businessRegistrationInfo.value = it }
            .onFailure { errorHandlerHelper.sendError(it) }
    }
}

enum class CenterSignUpStep(val step: Int) {
    NAME(1), PHONE_NUMBER(2), BUSINESS_REGISTRATION(3), ID_PASSWORD(4);

    companion object {
        fun findStep(step: Int): CenterSignUpStep {
            return CenterSignUpStep.entries.first { it.step == step }
        }
    }
}