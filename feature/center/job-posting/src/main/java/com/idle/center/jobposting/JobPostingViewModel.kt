package com.idle.center.jobposting

import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination.CenterJobPostingComplete
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.center.job.posting.R
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.LifeAssistance
import com.idle.domain.model.job.MentalStatus
import com.idle.domain.model.job.PayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobPostingViewModel @Inject constructor() : BaseViewModel() {

    private val _weekDays = MutableStateFlow<Set<DayOfWeek>>(setOf())
    val weekDays = _weekDays.asStateFlow()

    private val _payType = MutableStateFlow<PayType?>(null)
    val payType = _payType.asStateFlow()

    private val _payAmount = MutableStateFlow("")
    val payAmount = _payAmount.asStateFlow()

    private val _jobPostingStep = MutableStateFlow(JobPostingStep.TIME_PAYMENT)
    val registerProcess = _jobPostingStep.asStateFlow()

    private val _roadNameAddress = MutableStateFlow("")
    val roadNameAddress = _roadNameAddress.asStateFlow()

    private val _lotNumberAddress = MutableStateFlow("")

    private val _detailAddress = MutableStateFlow("")
    val detailAddress = _detailAddress.asStateFlow()

    private val _gender = MutableStateFlow(Gender.NONE)
    internal val gender = _gender.asStateFlow()

    private val _birthYear = MutableStateFlow("")
    val birthYear = _birthYear.asStateFlow()

    private val _weight = MutableStateFlow("")
    val weight = _weight.asStateFlow()

    private val _careLevel = MutableStateFlow("")
    val careLevel = _careLevel.asStateFlow()

    private val _mentalStatus = MutableStateFlow(MentalStatus.UNKNOWN)
    val mentalStatus = _mentalStatus.asStateFlow()

    private val _disease = MutableStateFlow("")
    val disease = _disease.asStateFlow()

    private val _isMealAssistance = MutableStateFlow<Boolean?>(null)
    val isMealAssistance = _isMealAssistance.asStateFlow()

    private val _isBowelAssistance = MutableStateFlow<Boolean?>(null)
    val isBowelAssistance = _isBowelAssistance.asStateFlow()

    private val _isWalkingAssistance = MutableStateFlow<Boolean?>(null)
    val isWalkingAssistance = _isWalkingAssistance.asStateFlow()

    private val _lifeAssistance = MutableStateFlow<Set<LifeAssistance>>(setOf())
    val lifeAssistance = _lifeAssistance.asStateFlow()

    private val _speciality = MutableStateFlow("")
    val speciality = _speciality.asStateFlow()

    private val _isExperiencePreferred = MutableStateFlow<Boolean?>(null)
    val isExperiencePreferred = _isExperiencePreferred.asStateFlow()

    private val _applyMethod = MutableStateFlow<Set<ApplyMethod>>(setOf())
    val applyMethod = _applyMethod.asStateFlow()

    private val _applyDeadlineChipState = MutableStateFlow<ApplyDeadlineChipState?>(null)
    val applyDeadlineChipState = _applyDeadlineChipState.asStateFlow()

    private val _applyDeadline = MutableStateFlow<String>("")
    val applyDeadline = _applyDeadline.asStateFlow()

    internal fun setWeekDays(dayOfWeek: DayOfWeek) {
        _weekDays.value = _weekDays.value.toMutableSet().apply {
            if (dayOfWeek in this) remove(dayOfWeek)
            else add(dayOfWeek)
        }.toSet()
    }

    internal fun setPayType(payType: PayType) {
        _payType.value = payType
    }

    internal fun setPayAmount(payAmount: String) {
        _payAmount.value = payAmount
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

    internal fun setDetailAddress(address: String) {
        _detailAddress.value = address
    }

    internal fun setGender(gender: Gender) {
        _gender.value = gender
    }

    internal fun setBirthYear(birthYear: String) {
        _birthYear.value = birthYear
    }

    internal fun setWeight(weight: String) {
        _weight.value = weight
    }

    internal fun setCareLevel(careLevel: String) {
        _careLevel.value = careLevel
    }

    internal fun setMentalStatus(mentalStatus: MentalStatus) {
        _mentalStatus.value = mentalStatus
    }

    internal fun setDisease(disease: String) {
        _disease.value = disease
    }

    internal fun setMealAssistance(necessary: Boolean) {
        _isMealAssistance.value = necessary
    }

    internal fun setBowelAssistance(necessary: Boolean) {
        _isBowelAssistance.value = necessary
    }

    internal fun setWalkingAssistance(necessary: Boolean) {
        _isWalkingAssistance.value = necessary
    }

    internal fun setLifeAssistance(lifeAssistance: LifeAssistance) {
        _lifeAssistance.value = _lifeAssistance.value.toMutableSet().apply {
            if (lifeAssistance in this) remove(lifeAssistance)
            else add(lifeAssistance)
        }.toSet()
    }

    internal fun setSpeciality(speciality: String) {
        _speciality.value = speciality
    }

    internal fun setExperiencePreferred(preferred: Boolean) {
        _isExperiencePreferred.value = preferred
    }

    internal fun setApplyMethod(applyMethod: ApplyMethod) {
        _applyMethod.value = _applyMethod.value.toMutableSet().apply {
            if (applyMethod in this) remove(applyMethod)
            else add(applyMethod)
        }.toSet()
    }

    internal fun setApplyDeadlineChipState(chipState: ApplyDeadlineChipState) {
        _applyDeadlineChipState.value = chipState
    }

    internal fun setApplyDeadline(applyDeadline: String) {
        _applyDeadline.value = applyDeadline
    }

    internal fun postJobPosting() = viewModelScope.launch {
        baseEvent(
            CareBaseEvent.NavigateTo(
                destination = CenterJobPostingComplete,
                popUpTo = R.id.jobPostingFragment
            )
        )
    }
}

enum class JobPostingStep(val step: Int) {
    TIME_PAYMENT(1), ADDRESS(2), CUSTOMER_INFORMATION(3),
    CUSTOMER_REQUIREMENT(4), ADDITIONAL_INFO(5), SUMMARY(6);

    companion object {
        fun findStep(step: Int): JobPostingStep {
            return JobPostingStep.entries.first { it.step == step }
        }
    }
}

enum class ApplyDeadlineChipState(val displayName: String) {
    UNTIL_HIRED("채용시까지"),
    SET_DEADLINE("마감일 설정")
}