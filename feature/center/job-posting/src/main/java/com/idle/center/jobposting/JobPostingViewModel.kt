package com.idle.center.jobposting

import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.PayType
import com.idle.domain.model.job.PayType.HOURLY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class JobPostingViewModel @Inject constructor() : BaseViewModel() {

    private val _weekDays = MutableStateFlow<Set<DayOfWeek>>(setOf())
    val weekDays = _weekDays.asStateFlow()

    private val _payType = MutableStateFlow<PayType>(HOURLY)
    val payType = _payType.asStateFlow()

    private val _jobPostingStep = MutableStateFlow(JobPostingStep.TIMEPAYMENT)
    val registerProcess = _jobPostingStep.asStateFlow()

    private val _roadNameAddress = MutableStateFlow("")
    val roadNameAddress = _roadNameAddress.asStateFlow()

    private val _lotNumberAddress = MutableStateFlow("")

    internal fun setWeekDays(dayOfWeek: DayOfWeek) {
        _weekDays.value = _weekDays.value.toMutableSet().apply {
            if (dayOfWeek in this) remove(dayOfWeek)
            else add(dayOfWeek)
        }.toSet()
    }

    internal fun setPayType(payType: PayType) {
        _payType.value = payType
    }

    internal fun setJobPostingStep(step: JobPostingStep) {
        _jobPostingStep.value = step
    }

    internal fun setRoadNameAddress(address: String) {
        _roadNameAddress.value = address
    }

    internal fun setLotNumberAddress(address: String) {
        _lotNumberAddress.value = address
    }
}

enum class JobPostingStep(val step: Int) {
    TIMEPAYMENT(1), ADDRESS(2), CUSTOMERINFORMATION(3),
    CUSTOMERREQUIREMENT(4), ADDITIONALINFO(5), SUMMARY(6);

    companion object {
        fun findStep(step: Int): JobPostingStep {
            return JobPostingStep.entries.first { it.step == step }
        }
    }
}