package com.idle.signup.worker

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.analytics.AnalyticsHelper
import com.idle.binding.EventHelper
import com.idle.common.suspendRunCatching
import com.idle.domain.model.CountDownTimer
import com.idle.domain.model.CountDownTimer.Companion.SECONDS_PER_MINUTE
import com.idle.domain.model.CountDownTimer.Companion.TICK_INTERVAL
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.error.HttpResponseStatus
import com.idle.domain.repositorry.AuthRepository
import com.idle.domain.repositorry.ProfileRepository
import com.idle.domain.util.formatPhoneNumber
import com.idle.navigation.DeepLinkDestination.SignUpComplete
import com.idle.navigation.DeepLinkDestination.WorkerHome
import com.idle.navigation.NavigationHelper
import com.idle.signup.R
import com.idle.signup.worker.WorkerSignUpStep.PHONE_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerSignUpViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val countDownTimer: CountDownTimer,
    private val analyticsHelper: AnalyticsHelper,
    private val errorHelper: ErrorHelper,
    val eventHelper: EventHelper,
    val navigationHelper: NavigationHelper,
) : ViewModel() {

    private val _signUpStep = MutableStateFlow<WorkerSignUpStep>(PHONE_NUMBER)
    internal val signUpStep = _signUpStep.asStateFlow()

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

    private val _isAuthCodeError = MutableStateFlow(false)
    val isAuthCodeError = _isAuthCodeError.asStateFlow()

    private val _workerName = MutableStateFlow("")
    internal val workerName = _workerName.asStateFlow()

    private val _birthYear = MutableStateFlow("")
    val birthYear = _birthYear.asStateFlow()

    private val _gender = MutableStateFlow(Gender.NONE)
    internal val gender = _gender.asStateFlow()

    private val _roadNameAddress = MutableStateFlow("")
    val roadNameAddress = _roadNameAddress.asStateFlow()

    private val _lotNumberAddress = MutableStateFlow("")

    internal fun setWorkerSignUpStep(step: WorkerSignUpStep) {
        _signUpStep.value = step
    }

    internal fun setWorkerPhoneNumber(phoneNumber: String) {
        if (phoneNumber.isDigitsOnly() && phoneNumber.length <= 11) {
            _workerPhoneNumber.value = phoneNumber
        }
    }

    internal fun setWorkerAuthCode(certificateNumber: String) {
        _workerAuthCode.value = certificateNumber
        _isAuthCodeError.value = false
    }

    internal fun setWorkerName(name: String) {
        _workerName.value = name
    }

    internal fun setBirthYear(birthYear: String) {
        if (birthYear.length <= 4) {
            _birthYear.value = birthYear
        }
    }

    internal fun setGender(gender: Gender) {
        _gender.value = gender
    }

    internal fun setRoadNameAddress(address: String) {
        _roadNameAddress.value = address
    }

    internal fun setLotNumberAddress(address: String) {
        _lotNumberAddress.value = address
    }

    internal fun sendPhoneNumber() = viewModelScope.launch {
        suspendRunCatching {
            authRepository.sendPhoneNumber(formatPhoneNumber(_workerPhoneNumber.value))
        }.onSuccess {
            startTimer()
        }.onFailure { errorHelper.sendError(it) }
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
        suspendRunCatching {
            authRepository.signInWorker(
                phoneNumber = formatPhoneNumber(_workerPhoneNumber.value),
                authCode = _workerAuthCode.value,
            )
        }.onSuccess {
            suspendRunCatching {
                profileRepository.getWorkerId()
            }.onSuccess { analyticsHelper.setUserId(it) }
            navigationHelper.navigateTo(
                com.idle.navigation.NavigationEvent.To(
                    WorkerHome,
                    R.id.workerSignUpFragment
                )
            )
        }.onFailure {
            suspendRunCatching {
                authRepository.confirmAuthCode(
                    formatPhoneNumber(_workerPhoneNumber.value),
                    _workerAuthCode.value
                )
            }.onSuccess {
                cancelTimer()
                _isConfirmAuthCode.value = true
                _signUpStep.value = WorkerSignUpStep.findStep(PHONE_NUMBER.step + 1)
            }.onFailure {
                if (it is HttpResponseException && it.status == HttpResponseStatus.BadRequest) {
                    _isAuthCodeError.value = true
                    return@launch
                }

                errorHelper.sendError(it)
            }
        }
    }

    internal fun signUpWorker() = viewModelScope.launch {
        suspendRunCatching {
            authRepository.signUpWorker(
                name = _workerName.value,
                birthYear = _birthYear.value.toIntOrNull() ?: return@suspendRunCatching,
                genderType = _gender.value.name,
                phoneNumber = formatPhoneNumber(_workerPhoneNumber.value),
                roadNameAddress = _roadNameAddress.value,
                lotNumberAddress = _lotNumberAddress.value,
            )
        }.onSuccess {
            suspendRunCatching {
                profileRepository.getWorkerId()
            }.onSuccess { analyticsHelper.setUserId(it) }
            navigationHelper.navigateTo(
                com.idle.navigation.NavigationEvent.To(
                    SignUpComplete,
                    R.id.workerSignUpFragment
                )
            )
        }.onFailure { errorHelper.sendError(it) }
    }
}

enum class WorkerSignUpStep(val step: Int) {
    PHONE_NUMBER(1), INFO(2), ADDRESS(3);

    companion object {
        fun findStep(step: Int): WorkerSignUpStep {
            return WorkerSignUpStep.entries.first { it.step == step }
        }
    }
}
