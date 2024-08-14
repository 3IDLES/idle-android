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

    internal fun setWithdrawalStep(step: WithdrawalStep) {
        _withdrawalStep.value = step
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