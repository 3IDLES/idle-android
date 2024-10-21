package com.idle.center.jobposting

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination.CenterJobPostingPostComplete
import com.idle.binding.EventHandlerHelper
import com.idle.binding.MainEvent
import com.idle.binding.NavigationEvent
import com.idle.binding.NavigationHelper
import com.idle.binding.base.BaseViewModel
import com.idle.center.job.posting.post.R
import com.idle.compose.JobPostingBottomSheetType
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.error.ErrorHandlerHelper
import com.idle.domain.model.jobposting.ApplyDeadlineType
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.DayOfWeek
import com.idle.domain.model.jobposting.LifeAssistance
import com.idle.domain.model.jobposting.MentalStatus
import com.idle.domain.model.jobposting.PayType
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.usecase.jobposting.PostJobPostingUseCase
import com.idle.domain.usecase.profile.GetLocalMyCenterProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class JobPostingViewModel @Inject constructor(
    private val getLocalMyCenterProfileUseCase: GetLocalMyCenterProfileUseCase,
    private val postJobPostingUseCase: PostJobPostingUseCase,
    private val errorHandlerHelper: ErrorHandlerHelper,
    val eventHandlerHelper: EventHandlerHelper,
    val navigationHelper: NavigationHelper,
) : BaseViewModel() {
    private val _profile = MutableStateFlow<CenterProfile?>(null)
    val profile = _profile.asStateFlow()

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

    @OptIn(FlowPreview::class)
    val isMinimumWageError = _payAmount
        .debounce(500L)
        .map {
            if (it.isNotBlank() && it.isDigitsOnly()) {
                it.toInt() < MINIMUM_WAGE
            } else {
                false
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )

    init {
        getMyCenterProfile()
    }

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
                    eventHandlerHelper.sendEvent(MainEvent.ShowSnackBar("근무 시작 시간은 근무 종료 시간보다 빨라야 합니다."))
                }
            } catch (e: DateTimeParseException) {
                eventHandlerHelper.sendEvent(MainEvent.ShowSnackBar("근무 시작 시간은 근무 종료 시간보다 빨라야 합니다."))
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
                    eventHandlerHelper.sendEvent(MainEvent.ShowSnackBar("근무 종료 시간은 근무 시작 시간보다 빨라야 합니다."))
                }
            } catch (e: DateTimeParseException) {
                eventHandlerHelper.sendEvent(MainEvent.ShowSnackBar("근무 종료 시간은 근무 시작 시간보다 빨라야 합니다."))
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
                    eventHandlerHelper.sendEvent(MainEvent.ShowSnackBar("급여 형식이 잘못되었습니다. 숫자로 입력해주세요."))
                    return@launch
                },
                roadNameAddress = _roadNameAddress.value,
                lotNumberAddress = _lotNumberAddress.value,
                clientName = _clientName.value,
                gender = _gender.value,
                birthYear = _birthYear.value.toIntOrNull() ?: let {
                    eventHandlerHelper.sendEvent(MainEvent.ShowSnackBar("올바른 출생년도를 입력해주세요."))
                    return@launch
                },
                weight = _weight.value.toIntOrNull(),
                careLevel = _careLevel.value.toIntOrNull() ?: let {
                    eventHandlerHelper.sendEvent(MainEvent.ShowSnackBar("올바른 요양 등급을 입력해주세요."))
                    return@launch
                },
                mentalStatus = _mentalStatus.value,
                disease = _disease.value.ifBlank { null },
                isMealAssistance = _isMealAssistance.value ?: let {
                    eventHandlerHelper.sendEvent(MainEvent.ShowSnackBar("식사 보조 여부를 선택해주세요."))
                    return@launch
                },
                isBowelAssistance = _isBowelAssistance.value ?: let {
                    eventHandlerHelper.sendEvent(MainEvent.ShowSnackBar("배변 보조 여부를 선택해주세요."))
                    return@launch
                },
                isWalkingAssistance = _isWalkingAssistance.value ?: let {
                    eventHandlerHelper.sendEvent(MainEvent.ShowSnackBar("이동 보조 여부를 선택해주세요."))
                    return@launch
                },
                lifeAssistance = _lifeAssistance.value.toList().sortedBy { it.ordinal }
                    .takeIf { it.isNotEmpty() } ?: listOf(LifeAssistance.NONE),
                extraRequirement = _extraRequirement.value.ifBlank { null },
                isExperiencePreferred = _isExperiencePreferred.value ?: let {
                    eventHandlerHelper.sendEvent(MainEvent.ShowSnackBar("경력 우대 여부를 선택해주세요."))
                    return@launch
                },
                applyMethod = _applyMethod.value.toList()
                    .sortedBy { it.ordinal },
                applyDeadLineType = _applyDeadlineType.value ?: ApplyDeadlineType.UNLIMITED,
                applyDeadline = _applyDeadline.value?.toString(),
            ).onSuccess {
                navigationHelper.navigateTo(
                    NavigationEvent.NavigateTo(
                        destination = CenterJobPostingPostComplete,
                        popUpTo = R.id.jobPostingPostFragment
                    )
                )
            }.onFailure { errorHandlerHelper.sendError(it) }
        }
    }

    private fun getMyCenterProfile() = viewModelScope.launch {
        getLocalMyCenterProfileUseCase().onSuccess {
            _profile.value = it
        }.onFailure { errorHandlerHelper.sendError(it) }
    }

    companion object {
        private const val MINIMUM_WAGE = 9860
    }
}

enum class JobPostingStep(val step: Int, val topBarStep: Int) {
    TIME_PAYMENT(1, 0),
    ADDRESS(2, 0),
    CUSTOMER_INFORMATION(3, 0),
    CUSTOMER_REQUIREMENT(4, 0),
    ADDITIONAL_INFO(5, 0),
    SUMMARY(6, 1),
    PREVIEW(7, 2),
    ;

    companion object {
        fun findStep(step: Int): JobPostingStep {
            return JobPostingStep.entries.first { it.step == step }
        }
    }
}