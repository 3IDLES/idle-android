package com.idle.withdrawal

import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.CountDownTimer
import com.idle.domain.model.CountDownTimer.Companion.SECONDS_PER_MINUTE
import com.idle.domain.model.CountDownTimer.Companion.TICK_INTERVAL
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.usecase.auth.ConfirmAuthCodeUseCase
import com.idle.domain.usecase.auth.SendPhoneNumberUseCase
import com.idle.domain.usecase.auth.WithdrawalCenterUseCase
import com.idle.domain.usecase.auth.WithdrawalWorkerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawalViewModel @Inject constructor(
    private val sendPhoneNumberUseCase: SendPhoneNumberUseCase,
    private val confirmAuthCodeUseCase: ConfirmAuthCodeUseCase,
    private val withdrawalCenterUseCase: WithdrawalCenterUseCase,
    private val withdrawalWorkerUseCase: WithdrawalWorkerUseCase,
    private val countDownTimer: CountDownTimer,
) : BaseViewModel() {
    private val _withdrawalStep = MutableStateFlow<WithdrawalStep>(WithdrawalStep.REASON)
    internal val withdrawalStep = _withdrawalStep.asStateFlow()

    private val _withdrawalReason = MutableStateFlow<Set<WithdrawalReason>>(setOf())
    private val _phoneNumber = MutableStateFlow("")

    private val _authCodeTimerMinute = MutableStateFlow("")
    val authCodeTimerMinute = _authCodeTimerMinute.asStateFlow()

    private val _authCodeTimerSeconds = MutableStateFlow("")
    val authCodeTimerSeconds = _authCodeTimerSeconds.asStateFlow()

    private var timerJob: Job? = null

    private val _authCode = MutableStateFlow("")

    private val _isConfirmAuthCode = MutableStateFlow(false)
    val isConfirmAuthCode = _isConfirmAuthCode.asStateFlow()

    internal fun setWithdrawalStep(step: WithdrawalStep) {
        _withdrawalStep.value = step
    }

    internal fun setWithdrawalReason(reason: WithdrawalReason) {
        _withdrawalReason.value = _withdrawalReason.value.toMutableSet().apply {
            if (reason in this) remove(reason)
            else add(reason)
        }.toSet()
    }

    internal fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    internal fun setAuthCode(authCode: String) {
        _authCode.value = authCode
    }

    internal fun sendPhoneNumber() = viewModelScope.launch {
        sendPhoneNumberUseCase(_phoneNumber.value)
            .onSuccess { startTimer() }
            .onFailure { handleFailure(it as HttpResponseException) }
    }

    private fun startTimer() {
        cancelTimer()

        timerJob = viewModelScope.launch {
            countDownTimer.start(limitTime = TICK_INTERVAL * SECONDS_PER_MINUTE * 5)
                .collect { timeMillis -> updateTimerDisplay(timeMillis) }
        }
    }

    private fun updateTimerDisplay(timeMillis: Long) {
        val minutes =
            (timeMillis / (TICK_INTERVAL * SECONDS_PER_MINUTE)).toString().padStart(2, '0')
        val seconds =
            ((timeMillis % (TICK_INTERVAL * SECONDS_PER_MINUTE)) / TICK_INTERVAL).toString()
                .padStart(2, '0')

        _authCodeTimerMinute.value = minutes
        _authCodeTimerSeconds.value = seconds
    }

    private fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    internal fun confirmAuthCode() = viewModelScope.launch {
        confirmAuthCodeUseCase(_phoneNumber.value, _authCode.value)
            .onSuccess {
                cancelTimer()
                _isConfirmAuthCode.value = true
            }
            .onFailure { handleFailure(it as HttpResponseException) }
    }

    internal fun withdrawal(userType: UserType) = viewModelScope.launch {
        when (userType) {
            UserType.CENTER -> withdrawalCenter()
            UserType.WORKER -> withdrawalWorker()
        }
    }

    private suspend fun withdrawalCenter() {
        withdrawalCenterUseCase(
            reason = _withdrawalReason.value
                .sortedBy { it.ordinal }
                .joinToString("|"),
            password = "testpassword1234"
        ).onSuccess {
            baseEvent(
                CareBaseEvent.NavigateTo(
                    destination = DeepLinkDestination.Auth,
                    popUpTo = com.idle.withdrawal.R.id.withdrawalFragment,
                )
            )
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    private suspend fun withdrawalWorker() {
        withdrawalWorkerUseCase(
            _withdrawalReason.value
                .sortedBy { it.ordinal }
                .joinToString("|"),
        ).onSuccess {
            baseEvent(CareBaseEvent.NavigateToAuthWithClearBackStack)
        }.onFailure { handleFailure(it as HttpResponseException) }
    }
}

enum class WithdrawalStep(val step: Int) {
    REASON(1), PHONENUMBER(2);

    companion object {
        fun findStep(step: Int): WithdrawalStep {
            return WithdrawalStep.entries.first { it.step == step }
        }
    }
}

enum class WithdrawalReason(val displayName: String) {
    MATCHING_NOT_SATISFACTORY("매칭이 잘 이루어지지 않음"),
    INCONVENIENT_PLATFORM_USE("플랫폼 사용에 불편함을 느낌"),
    NO_LONGER_USE_PLATFORM("플랫폼을 더 이상 사용할 이유가 없음"),
    USING_ANOTHER_PLATFORM("다른 플랫폼을 이용하고 있음"),
    LACK_OF_DESIRED_FEATURES("원하는 기능이 부재"),
    PRIVACY_CONCERNS("개인정보 보호 문제"),

    // 센터
    NO_LONGER_OPERATING_CENTER("센터를 더 이상 운영하지 않음"),

    // 요양 보호사
    NO_LONGER_WISH_TO_CONTINUE("더 이상 구직 의사가 없음");
}