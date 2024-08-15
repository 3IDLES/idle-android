package com.idle.withdrawal.step

sealed class WithdrawalEvent {
    data object WithdrawalSuccess : WithdrawalEvent()
}