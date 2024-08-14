package com.idle.withdrawal

import com.idle.binding.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WithdrawalViewModel @Inject constructor() : BaseViewModel() {
    private val _withdrawalStep = MutableStateFlow<WithdrawalStep>(WithdrawalStep.REASON)
    internal val withdrawalStep = _withdrawalStep.asStateFlow()

    private val _withdrawalReason = MutableStateFlow<Set<WithdrawalReason>>(setOf())

    internal fun setWithdrawalStep(step: WithdrawalStep) {
        _withdrawalStep.value = step
    }

    internal fun setWithdrawalReason(reason: WithdrawalReason) {
        _withdrawalReason.value = _withdrawalReason.value.toMutableSet().apply {
            if (reason in this) remove(reason)
            else add(reason)
        }.toSet()
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