package com.idle.withdrawal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.analytics.AnalyticsHelper
import com.idle.binding.EventHelper
import com.idle.binding.MainEvent
import com.idle.domain.model.CountDownTimer
import com.idle.domain.model.CountDownTimer.Companion.SECONDS_PER_MINUTE
import com.idle.domain.model.CountDownTimer.Companion.TICK_INTERVAL
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.error.ApiErrorCode
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.repositorry.AuthRepository
import com.idle.domain.repositorry.ChatRepository
import com.idle.domain.util.formatPhoneNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawalViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val chatRepository: ChatRepository,
    private val countDownTimer: CountDownTimer,
    private val analyticsHelper: AnalyticsHelper,
    private val errorHelper: ErrorHelper,
    private val eventHelper: EventHelper,
    val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
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

    private val _inconvenientReason = MutableStateFlow<String>("")
    val inconvenientReason = _inconvenientReason.asStateFlow()

    private val _anotherPlatformReason = MutableStateFlow<String>("")
    val anotherPlatformReason = _anotherPlatformReason.asStateFlow()

    private val _lackFeaturesReason = MutableStateFlow<String>("")
    val lackFeaturesReason = _lackFeaturesReason.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

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
        if (phoneNumber.length <= 11) {
            _phoneNumber.value = phoneNumber
        }
    }

    internal fun setAuthCode(authCode: String) {
        if (authCode.length > 6) {
            return
        }

        _authCode.value = authCode
    }

    internal fun sendPhoneNumber() = viewModelScope.launch {
        authRepository.sendPhoneNumber(formatPhoneNumber(_phoneNumber.value))
            .onSuccess { startTimer() }
            .onFailure { errorHelper.sendError(it) }
    }

    internal fun setInconvenientReason(reason: String) {
        _inconvenientReason.value = reason
    }

    internal fun setAnotherPlatformReason(reason: String) {
        _anotherPlatformReason.value = reason
    }

    internal fun setLackFeaturesReason(reason: String) {
        _lackFeaturesReason.value = reason
    }

    internal fun setPassword(password: String) {
        _password.value = password
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
        authRepository.confirmAuthCode(formatPhoneNumber(_phoneNumber.value), _authCode.value)
            .onSuccess {
                cancelTimer()
                _isConfirmAuthCode.value = true
            }
            .onFailure { errorHelper.sendError(it) }
    }

    internal fun withdrawal(userType: UserType) = viewModelScope.launch {
        when (userType) {
            UserType.CENTER -> withdrawalCenter()
            UserType.WORKER -> withdrawalWorker()
        }
    }

    private suspend fun withdrawalCenter() {
        authRepository.withdrawalCenter(
            reason = _withdrawalReason.value
                .sortedBy { it.ordinal }
                .map { reason ->
                    when (reason) {
                        WithdrawalReason.INCONVENIENT_PLATFORM_USE -> "${reason} : ${_inconvenientReason.value}"
                        WithdrawalReason.USING_ANOTHER_PLATFORM -> "${reason} : ${_anotherPlatformReason.value}"
                        WithdrawalReason.LACK_OF_DESIRED_FEATURES -> "${reason} : ${_lackFeaturesReason.value}"
                        else -> reason
                    }
                }
                .joinToString("|"),
            password = password.value
        ).onSuccess {
            analyticsHelper.setUserId(null)
            chatRepository.disconnectWebSocket()
            navigationHelper.navigateTo(
                com.idle.navigation.NavigationEvent.ToAuthWithClearBackStack(
                    "회원탈퇴가 완료되었어요."
                )
            )
        }.onFailure {
            val exception = it as HttpResponseException
            if (exception.apiErrorCode == ApiErrorCode.InvalidParameter) {
                eventHelper.sendEvent(MainEvent.ShowToast("비밀번호가 맞지 않습니다."))
            } else {
                errorHelper.sendError(it)
            }
        }
    }

    private suspend fun withdrawalWorker() {
        authRepository.withdrawalWorker(
            _withdrawalReason.value
                .sortedBy { it.ordinal }
                .joinToString("|"),
        ).onSuccess {
            analyticsHelper.setUserId(null)
            chatRepository.disconnectWebSocket()
            navigationHelper.navigateTo(
                com.idle.navigation.NavigationEvent.ToAuthWithClearBackStack(
                    "회원탈퇴가 완료되었어요."
                )
            )
        }.onFailure { errorHelper.sendError(it) }
    }
}

enum class WithdrawalStep(val step: Int) {
    REASON(1), PHONE_NUMBER(2), PASSWORD(2);

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
