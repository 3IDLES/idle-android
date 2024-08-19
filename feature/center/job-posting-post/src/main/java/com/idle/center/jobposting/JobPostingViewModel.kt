package com.idle.center.jobposting

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination.CenterJobPostingPostComplete
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.center.job.posting.post.R
import com.idle.compose.JobPostingBottomSheetType
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyDeadlineType
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.LifeAssistance
import com.idle.domain.model.job.MentalStatus
import com.idle.domain.model.job.PayType
import com.idle.domain.usecase.jobposting.PostJobPostingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class JobPostingViewModel @Inject constructor(
    private val postJobPostingUseCase: PostJobPostingUseCase,
) : BaseViewModel() {

    private val _weekDays = MutableStateFlow<Set<DayOfWeek>>(setOf())
    val weekDays = _weekDays.asStateFlow()

    private val _workStartTime = MutableStateFlow("")
    val workStartTime = _workStartTime.asStateFlow()

    private val _workEndTime = MutableStateFlow("")
    val workEndTime = _workEndTime.asStateFlow()

    private val _payType = MutableStateFlow<PayType?>(null)
    val payType = _payType.asStateFlow()

    private val _payAmount = MutableStateFlow("")
    val payAmount = _payAmount.asStateFlow()

    private val _jobPostingStep = MutableStateFlow(JobPostingStep.TIME_PAYMENT)
    val registerProcess = _jobPostingStep.asStateFlow()

    private val _roadNameAddress = MutableStateFlow("")
    val roadNameAddress = _roadNameAddress.asStateFlow()

    private val _lotNumberAddress = MutableStateFlow("")
    val lotNumberAddress = _lotNumberAddress.asStateFlow()

    private val _clientName = MutableStateFlow("")
    val clientName = _clientName.asStateFlow()

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

    private val _extraRequirement = MutableStateFlow("")
    val extraRequirement = _extraRequirement.asStateFlow()

    private val _isExperiencePreferred = MutableStateFlow<Boolean?>(null)
    val isExperiencePreferred = _isExperiencePreferred.asStateFlow()

    private val _applyMethod = MutableStateFlow<Set<ApplyMethod>>(setOf())
    val applyMethod = _applyMethod.asStateFlow()

    private val _applyDeadlineType = MutableStateFlow<ApplyDeadlineType?>(null)
    val applyDeadlineType = _applyDeadlineType.asStateFlow()

    private val _applyDeadline = MutableStateFlow<LocalDate?>(null)
    val applyDeadline = _applyDeadline.asStateFlow()

    private val seoulZoneId = ZoneId.of("Asia/Seoul")
    private val currentDate = ZonedDateTime.now(seoulZoneId).toLocalDate()

    private val _calendarDate = MutableStateFlow(currentDate)
    val calendarDate = _calendarDate.asStateFlow()

    private val _isEditState = MutableStateFlow(false)
    val isEditState = _isEditState.asStateFlow()

    private val _bottomSheetType = MutableStateFlow<JobPostingBottomSheetType?>(null)
    val bottomSheetType = _bottomSheetType.asStateFlow()

    internal fun setWeekDays(dayOfWeek: DayOfWeek) {
        _weekDays.value = _weekDays.value.toMutableSet().apply {
            if (dayOfWeek in this) remove(dayOfWeek)
            else add(dayOfWeek)
        }.toSet()
    }

    internal fun clearWeekDays() {
        _weekDays.value = setOf()
    }

    internal fun setPayType(payType: PayType) {
        _payType.value = payType
    }

    internal fun setPayAmount(payAmount: String) {
        _payAmount.value = payAmount
    }

    internal fun setWorkStartTime(time: String) {
        if (_workEndTime.value.isNotEmpty()) {
            try {
                val startTime = LocalTime.parse(time)
                val endTime = LocalTime.parse(_workEndTime.value)
                if (startTime.isBefore(endTime)) {
                    _workStartTime.value = time
                } else {
                    baseEvent(CareBaseEvent.Error("근무 시작 시간은 근무 종료 시간보다 빨라야 합니다."))
                }
            } catch (e: DateTimeParseException) {
                baseEvent(CareBaseEvent.Error("근무 시작 시간은 근무 종료 시간보다 빨라야 합니다."))
            }

            return
        }

        _workStartTime.value = time
    }

    internal fun setWorkEndTime(time: String) {
        if (_workStartTime.value.isNotEmpty()) {
            try {
                val endTime = LocalTime.parse(time)
                val startTime = LocalTime.parse(_workStartTime.value)
                if (endTime.isAfter(startTime)) {
                    _workEndTime.value = time
                } else {
                    baseEvent(CareBaseEvent.Error("근무 종료 시간은 근무 시작 시간보다 빨라야 합니다."))
                }
            } catch (e: DateTimeParseException) {
                baseEvent(CareBaseEvent.Error("근무 종료 시간은 근무 시작 시간보다 빨라야 합니다."))
            }
            return
        }

        _workEndTime.value = time
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

    internal fun setClientName(name: String) {
        _clientName.value = name
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

    internal fun clearLifeAssistance() {
        _lifeAssistance.value = setOf()
    }

    internal fun setExtraRequirement(extraRequirement: String) {
        _extraRequirement.value = extraRequirement
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

    internal fun clearApplyMethod() {
        _applyMethod.value = setOf()
    }

    internal fun setApplyDeadlineType(chipState: ApplyDeadlineType) {
        _applyDeadlineType.value = chipState
    }

    internal fun setApplyDeadline(applyDeadline: LocalDate?) {
        _applyDeadline.value = applyDeadline
    }

    internal fun setCalendarMonth(month: Int) {
        _calendarDate.value = _calendarDate.value.withMonth(month)
    }

    internal fun setEditState(editState: Boolean) {
        _isEditState.value = editState
    }

    internal fun setBottomSheetType(sheetType: JobPostingBottomSheetType) {
        _bottomSheetType.value = sheetType
    }

    internal fun postJobPosting() {
        viewModelScope.launch {
            postJobPostingUseCase(
                weekdays = _weekDays.value.toList()
                    .sortedBy { it.ordinal },
                startTime = _workStartTime.value,
                endTime = _workEndTime.value,
                payType = _payType.value ?: PayType.UNKNOWN,
                payAmount = _payAmount.value.toIntOrNull() ?: let {
                    baseEvent(CareBaseEvent.Error("급여가 잘못되었습니다."))
                    return@launch
                },
                roadNameAddress = _roadNameAddress.value,
                lotNumberAddress = _lotNumberAddress.value,
                clientName = _clientName.value,
                gender = _gender.value,
                birthYear = _birthYear.value.toIntOrNull() ?: let {
                    baseEvent(CareBaseEvent.Error("출생년도가 잘못되었습니다."))
                    return@launch
                },
                weight = _weight.value.toIntOrNull(),
                careLevel = _careLevel.value.toIntOrNull() ?: let {
                    baseEvent(CareBaseEvent.Error("요양등급이 잘못되었습니다."))
                    return@launch
                },
                mentalStatus = _mentalStatus.value,
                disease = _disease.value.ifBlank { null },
                isMealAssistance = _isMealAssistance.value ?: let {
                    baseEvent(CareBaseEvent.Error("식사보조가 잘못되었습니다."))
                    return@launch
                },
                isBowelAssistance = _isBowelAssistance.value ?: let {
                    baseEvent(CareBaseEvent.Error("배변보조가 잘못되었습니다."))
                    return@launch
                },
                isWalkingAssistance = _isWalkingAssistance.value ?: let {
                    baseEvent(CareBaseEvent.Error("이동보조가 잘못되었습니다."))
                    return@launch
                },
                lifeAssistance = _lifeAssistance.value.toList()
                    .sortedBy { it.ordinal }
                    .takeIf { it.isNotEmpty() } ?: listOf(LifeAssistance.NONE),
                extraRequirement = _extraRequirement.value.ifBlank { null },
                isExperiencePreferred = _isExperiencePreferred.value ?: let {
                    baseEvent(CareBaseEvent.Error("경력 우대 여부가 잘못되었습니다."))
                    return@launch
                },
                applyMethod = _applyMethod.value.toList()
                    .sortedBy { it.ordinal },
                applyDeadLineType = _applyDeadlineType.value ?: ApplyDeadlineType.UNLIMITED,
                applyDeadline = _applyDeadline.value?.toString(),
            ).onSuccess {
                baseEvent(
                    CareBaseEvent.NavigateTo(
                        destination = CenterJobPostingPostComplete,
                        popUpTo = R.id.jobPostingPostFragment
                    )
                )
            }.onFailure { baseEvent(CareBaseEvent.Error(it.message.toString())) }
        }
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