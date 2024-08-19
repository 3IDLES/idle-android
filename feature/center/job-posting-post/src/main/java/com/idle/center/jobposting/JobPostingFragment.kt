@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package com.idle.center.jobposting

import android.util.Log
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.idle.center.job.edit.JobEditScreen
import com.idle.center.jobposting.step.AdditionalInfoScreen
import com.idle.center.jobposting.step.AddressScreen
import com.idle.center.jobposting.step.CustomerInformationScreen
import com.idle.center.jobposting.step.CustomerRequirementScreen
import com.idle.center.jobposting.step.JobPostingSummaryScreen
import com.idle.center.jobposting.step.TimePaymentScreen
import com.idle.compose.JobPostingBottomSheetType
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareBottomSheetLayout
import com.idle.designsystem.compose.component.CareButtonMedium
import com.idle.designsystem.compose.component.CareButtonRound
import com.idle.designsystem.compose.component.CareCalendar
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareWheelPicker
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyDeadlineType
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.LifeAssistance
import com.idle.domain.model.job.MentalStatus
import com.idle.domain.model.job.PayType
import com.idle.post.code.PostCodeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
internal class JobPostingFragment : BaseComposeFragment() {

    override val fragmentViewModel: JobPostingViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val jobPostingStep by registerProcess.collectAsStateWithLifecycle()
            val weekDays by weekDays.collectAsStateWithLifecycle()
            val workStartTime by workStartTime.collectAsStateWithLifecycle()
            val workEndTime by workEndTime.collectAsStateWithLifecycle()
            val payType by payType.collectAsStateWithLifecycle()
            val payAmount by payAmount.collectAsStateWithLifecycle()
            val roadNameAddress by roadNameAddress.collectAsStateWithLifecycle()
            val lotNumberAddress by lotNumberAddress.collectAsStateWithLifecycle()
            val clientName by clientName.collectAsStateWithLifecycle()
            val gender by gender.collectAsStateWithLifecycle()
            val birthYear by birthYear.collectAsStateWithLifecycle()
            val weight by weight.collectAsStateWithLifecycle()
            val careLevel by careLevel.collectAsStateWithLifecycle()
            val mentalStatus by mentalStatus.collectAsStateWithLifecycle()
            val disease by disease.collectAsStateWithLifecycle()
            val isMealAssistance by isMealAssistance.collectAsStateWithLifecycle()
            val isBowelAssistance by isBowelAssistance.collectAsStateWithLifecycle()
            val isWalkingAssistance by isWalkingAssistance.collectAsStateWithLifecycle()
            val lifeAssistance by lifeAssistance.collectAsStateWithLifecycle()
            val extraRequirement by extraRequirement.collectAsStateWithLifecycle()
            val isExperiencePreferred by isExperiencePreferred.collectAsStateWithLifecycle()
            val applyMethod by applyMethod.collectAsStateWithLifecycle()
            val applyDeadlineType by applyDeadlineType.collectAsStateWithLifecycle()
            val applyDeadline by applyDeadline.collectAsStateWithLifecycle()
            val isEditState by isEditState.collectAsStateWithLifecycle()
            val jobPostingBottomSheetType by bottomSheetType.collectAsStateWithLifecycle()
            val calendarDate by calendarDate.collectAsStateWithLifecycle()

            val postCodeDialog: PostCodeFragment? by lazy {
                PostCodeFragment().apply {
                    onDismissCallback = {
                        findNavController().currentBackStackEntry?.savedStateHandle?.let {
                            val roadName = it.get<String>("roadNameAddress")
                            val lotNumber = it.get<String>("lotNumberAddress")

                            fragmentViewModel.setRoadNameAddress(roadName ?: "")
                            fragmentViewModel.setLotNumberAddress(lotNumber ?: "")
                        }
                    }
                }
            }

            CareStateAnimator(
                targetState = isEditState,
                transitionCondition = isEditState
            ) { state ->
                if (state) {
                    JobEditScreen(
                        weekDays = weekDays,
                        workStartTime = workStartTime,
                        workEndTime = workEndTime,
                        fragmentManager = parentFragmentManager,
                        payType = payType!!,
                        payAmount = payAmount,
                        roadNameAddress = roadNameAddress,
                        lotNumberAddress = lotNumberAddress,
                        clientName = clientName,
                        gender = gender,
                        birthYear = birthYear,
                        weight = weight,
                        careLevel = careLevel,
                        mentalStatus = mentalStatus,
                        disease = disease,
                        isMealAssistance = isMealAssistance!!,
                        isBowelAssistance = isBowelAssistance!!,
                        isWalkingAssistance = isWalkingAssistance!!,
                        lifeAssistance = lifeAssistance,
                        extraRequirement = extraRequirement,
                        isExperiencePreferred = isExperiencePreferred!!,
                        applyMethod = applyMethod,
                        applyDeadlineType = applyDeadlineType!!,
                        applyDeadline = applyDeadline,
                        updateJobPosting = { editJobPosting ->
                            clearWeekDays()
                            clearLifeAssistance()
                            clearApplyMethod()
                            editJobPosting.let {
                                it.weekdays.forEach { setWeekDays(it) }
                                setWorkStartTime(it.startTime)
                                setWorkEndTime(it.endTime)
                                setPayType(it.payType)
                                setPayAmount(it.payAmount)
                                setClientName(it.clientName)
                                setGender(it.gender)
                                setBirthYear(it.birthYear)
                                it.weight?.let { weight -> setWeight(weight) }
                                setCareLevel(it.careLevel)
                                setMentalStatus(it.mentalStatus)
                                setDisease(it.disease)
                                setMealAssistance(it.isMealAssistance)
                                setBowelAssistance(it.isBowelAssistance)
                                setWalkingAssistance(it.isWalkingAssistance)
                                it.lifeAssistance.forEach { setLifeAssistance(it) }
                                it.extraRequirement?.let { extraRequirement ->
                                    setExtraRequirement(extraRequirement)
                                }
                                setExperiencePreferred(it.isExperiencePreferred)
                                it.applyMethod.forEach { setApplyMethod(it) }
                                setApplyDeadline(it.applyDeadline)
                                setApplyDeadlineType(it.applyDeadlineType)
                                setRoadNameAddress(it.roadNameAddress)
                                setLotNumberAddress(it.lotNumberAddress)
                            }

                            setEditState(false)
                        },
                        setEditState = ::setEditState,
                    )
                } else {
                    JobPostingScreen(
                        weekDays = weekDays,
                        workStartTime = workStartTime,
                        workEndTime = workEndTime,
                        payType = payType,
                        payAmount = payAmount,
                        roadNameAddress = roadNameAddress,
                        lotNumberAddress = lotNumberAddress,
                        clientName = clientName,
                        gender = gender,
                        birthYear = birthYear,
                        weight = weight,
                        careLevel = careLevel,
                        mentalStatus = mentalStatus,
                        disease = disease,
                        isMealAssistance = isMealAssistance,
                        isBowelAssistance = isBowelAssistance,
                        isWalkingAssistance = isWalkingAssistance,
                        lifeAssistance = lifeAssistance,
                        extraRequirement = extraRequirement,
                        isExperiencePreferred = isExperiencePreferred,
                        applyMethod = applyMethod,
                        applyDeadlineType = applyDeadlineType,
                        applyDeadline = applyDeadline,
                        calendarDate = calendarDate,
                        jobPostingStep = jobPostingStep,
                        bottomSheetType = jobPostingBottomSheetType,
                        setWeekDays = ::setWeekDays,
                        onWorkStartTimeChanged = ::setWorkStartTime,
                        onWorkEndTimeChanged = ::setWorkEndTime,
                        onPayTypeChanged = ::setPayType,
                        onPayAmountChanged = ::setPayAmount,
                        showPostCodeDialog = {
                            if (!(postCodeDialog?.isAdded == true || postCodeDialog?.isVisible == true)) {
                                postCodeDialog?.show(parentFragmentManager, "PostCodeFragment")
                            }
                        },
                        onClientNameChanged = ::setClientName,
                        onGenderChanged = ::setGender,
                        onWeightChanged = ::setWeight,
                        onMentalStatusChanged = ::setMentalStatus,
                        onBirthYearChanged = ::setBirthYear,
                        onCareLevelChanged = ::setCareLevel,
                        onDiseaseChanged = ::setDisease,
                        onMealAssistanceChanged = ::setMealAssistance,
                        onBowelAssistanceChanged = ::setBowelAssistance,
                        onWalkingAssistanceChanged = ::setWalkingAssistance,
                        onLifeAssistanceChanged = ::setLifeAssistance,
                        onExtraRequirementChanged = ::setExtraRequirement,
                        onExperiencePreferredChanged = ::setExperiencePreferred,
                        onApplyMethodChanged = ::setApplyMethod,
                        onApplyDeadlineTypeChanged = ::setApplyDeadlineType,
                        onApplyDeadlineChanged = ::setApplyDeadline,
                        onCalendarMonthChanged = ::setCalendarMonth,
                        postJobPosting = ::postJobPosting,
                        setJobPostingStep = ::setJobPostingStep,
                        setEditState = ::setEditState,
                        setBottomSheetType = ::setBottomSheetType,
                    )
                }
            }
        }
    }

    override fun handleError(message: String) {
        TODO("Not yet implemented")
    }
}

@ExperimentalMaterial3Api
@Composable
internal fun JobPostingScreen(
    weekDays: Set<DayOfWeek>,
    workStartTime: String,
    workEndTime: String,
    payType: PayType?,
    payAmount: String,
    roadNameAddress: String,
    lotNumberAddress: String,
    clientName: String,
    gender: Gender,
    birthYear: String,
    weight: String,
    careLevel: String,
    mentalStatus: MentalStatus,
    disease: String,
    isMealAssistance: Boolean?,
    isBowelAssistance: Boolean?,
    isWalkingAssistance: Boolean?,
    lifeAssistance: Set<LifeAssistance>,
    extraRequirement: String,
    isExperiencePreferred: Boolean?,
    applyMethod: Set<ApplyMethod>,
    applyDeadlineType: ApplyDeadlineType?,
    applyDeadline: LocalDate?,
    calendarDate: LocalDate,
    jobPostingStep: JobPostingStep,
    bottomSheetType: JobPostingBottomSheetType?,
    setWeekDays: (DayOfWeek) -> Unit,
    onWorkStartTimeChanged: (String) -> Unit,
    onWorkEndTimeChanged: (String) -> Unit,
    onPayTypeChanged: (PayType) -> Unit,
    onPayAmountChanged: (String) -> Unit,
    showPostCodeDialog: () -> Unit,
    onClientNameChanged: (String) -> Unit,
    onGenderChanged: (Gender) -> Unit,
    onBirthYearChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit,
    onCareLevelChanged: (String) -> Unit,
    onMentalStatusChanged: (MentalStatus) -> Unit,
    onDiseaseChanged: (String) -> Unit,
    onMealAssistanceChanged: (Boolean) -> Unit,
    onBowelAssistanceChanged: (Boolean) -> Unit,
    onWalkingAssistanceChanged: (Boolean) -> Unit,
    onLifeAssistanceChanged: (LifeAssistance) -> Unit,
    onExtraRequirementChanged: (String) -> Unit,
    onExperiencePreferredChanged: (Boolean) -> Unit,
    onApplyMethodChanged: (ApplyMethod) -> Unit,
    onApplyDeadlineChanged: (LocalDate) -> Unit,
    onApplyDeadlineTypeChanged: (ApplyDeadlineType) -> Unit,
    onCalendarMonthChanged: (Int) -> Unit,
    postJobPosting: () -> Unit,
    setJobPostingStep: (JobPostingStep) -> Unit,
    setEditState: (Boolean) -> Unit,
    setBottomSheetType: (JobPostingBottomSheetType) -> Unit,
) {
    val onBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val startDateTime by rememberSaveable { mutableStateOf(calendarDate) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )

    CareStateAnimator(
        targetState = jobPostingStep == JobPostingStep.SUMMARY,
        transitionCondition = jobPostingStep == JobPostingStep.SUMMARY,
        modifier = Modifier.fillMaxSize(),
    ) { isSummary ->
        if (isSummary) {
            JobPostingSummaryScreen(
                weekDays = weekDays,
                workStartTime = workStartTime,
                workEndTime = workEndTime,
                payType = payType,
                payAmount = payAmount,
                roadNameAddress = roadNameAddress,
                lotNumberAddress = lotNumberAddress,
                clientName = clientName,
                gender = gender,
                birthYear = birthYear,
                weight = weight,
                careLevel = careLevel,
                mentalStatus = mentalStatus,
                disease = disease,
                isMealAssistance = isMealAssistance,
                isBowelAssistance = isBowelAssistance,
                isWalkingAssistance = isWalkingAssistance,
                lifeAssistance = lifeAssistance,
                extraRequirement = extraRequirement,
                isExperiencePreferred = isExperiencePreferred,
                applyMethod = applyMethod,
                applyDeadline = applyDeadline,
                setEditState = setEditState,
                setJobPostingStep = setJobPostingStep,
                postJobPosting = postJobPosting,
            )
        } else {
            CareBottomSheetLayout(
                sheetState = sheetState,
                sheetContent = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        when (bottomSheetType) {
                            JobPostingBottomSheetType.WORK_START_TIME -> {
                                var localWorkStartAmPm by remember { mutableStateOf("오전") }
                                var localWorkStartHour by remember { mutableStateOf("01") }
                                var localWorkStartMinute by remember { mutableStateOf("00") }

                                Text(
                                    text = stringResource(id = R.string.work_start_time),
                                    style = CareTheme.typography.heading3,
                                    color = CareTheme.colors.gray900,
                                )

                                Spacer(modifier = Modifier.height(80.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CareWheelPicker(
                                        items = listOf("오전", "오후"),
                                        onItemSelected = {
                                            localWorkStartAmPm = it
                                            Log.d("test", it)
                                        },
                                        initIndex = if (workStartTime.isBlank() ||
                                            workStartTime.substring(0, 2).toInt() <= 12
                                        ) 0 else 1,
                                        modifier = Modifier.padding(end = 40.dp),
                                    )

                                    CareWheelPicker(
                                        items = (1..12).toList(),
                                        onItemSelected = { localWorkStartHour = it },
                                        initIndex = if (workStartTime.isBlank()) 0
                                        else if (workStartTime.substring(0, 2) == "00") 11
                                        else workStartTime.substring(0, 2).toInt() - 1,
                                        modifier = Modifier.padding(end = 10.dp),
                                    )

                                    Text(
                                        text = ":",
                                        style = CareTheme.typography.subtitle2,
                                        color = CareTheme.colors.gray900,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.width(20.dp),
                                    )

                                    CareWheelPicker(
                                        items = (0..50 step 10).toList(),
                                        onItemSelected = { localWorkStartMinute = it },
                                        initIndex = if (workStartTime.isBlank()) 0
                                        else (workStartTime.substring(3, 5).toInt() / 10),
                                        modifier = Modifier.padding(start = 10.dp),
                                    )
                                }

                                Spacer(modifier = Modifier.height(80.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    CareButtonMedium(
                                        text = stringResource(id = R.string.cancel),
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = CareTheme.colors.orange400
                                        ),
                                        containerColor = CareTheme.colors.white000,
                                        textColor = CareTheme.colors.orange500,
                                        onClick = {
                                            coroutineScope.launch {
                                                sheetState.hide()
                                            }
                                        },
                                        modifier = Modifier.weight(1f),
                                    )

                                    CareButtonMedium(
                                        text = stringResource(id = R.string.save),
                                        onClick = {
                                            coroutineScope.launch {
                                                val startTime =
                                                    "${
                                                        if (localWorkStartAmPm == "오전" && localWorkStartHour == "12") "00"
                                                        else if (localWorkStartAmPm == "오전") localWorkStartHour
                                                        else if (localWorkStartAmPm == "오후" && localWorkStartHour == "12") "12"
                                                        else localWorkStartHour.toInt() + 12
                                                    }" + ":${localWorkStartMinute}"
                                                onWorkStartTimeChanged(startTime)
                                                sheetState.hide()
                                            }
                                        },
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                            }

                            JobPostingBottomSheetType.WORK_END_TIME -> {
                                var localWorkEndAmPm by remember { mutableStateOf("오전") }
                                var localWorkEndHour by remember { mutableStateOf("01") }
                                var localWorkEndMinute by remember { mutableStateOf("00") }

                                Text(
                                    text = stringResource(id = R.string.work_end_time),
                                    style = CareTheme.typography.heading3,
                                    color = CareTheme.colors.gray900,
                                )

                                Spacer(modifier = Modifier.height(80.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CareWheelPicker(
                                        items = listOf("오전", "오후"),
                                        onItemSelected = {
                                            localWorkEndAmPm = it
                                            Log.d("test", it)
                                        },
                                        initIndex = if (workEndTime.isBlank() ||
                                            workEndTime.substring(0, 2).toInt() <= 12
                                        ) 0 else 1,
                                        modifier = Modifier.padding(end = 40.dp),
                                    )

                                    CareWheelPicker(
                                        items = (1..12).toList(),
                                        onItemSelected = { localWorkEndHour = it },
                                        initIndex = if (workEndTime.isBlank()) 0
                                        else if (workEndTime.substring(0, 2) == "00") 11
                                        else workEndTime.substring(0, 2).toInt() - 1,
                                        modifier = Modifier.padding(end = 10.dp),
                                    )

                                    Text(
                                        text = ":",
                                        style = CareTheme.typography.subtitle2,
                                        color = CareTheme.colors.gray900,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.width(20.dp),
                                    )

                                    CareWheelPicker(
                                        items = (0..50 step 10).toList(),
                                        onItemSelected = { localWorkEndMinute = it },
                                        initIndex = if (workEndTime.isBlank()) 0
                                        else (workEndTime.substring(3, 5).toInt() / 10),
                                        modifier = Modifier.padding(start = 10.dp),
                                    )
                                }

                                Spacer(modifier = Modifier.height(80.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    CareButtonMedium(
                                        text = stringResource(id = R.string.cancel),
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = CareTheme.colors.orange400
                                        ),
                                        containerColor = CareTheme.colors.white000,
                                        textColor = CareTheme.colors.orange500,
                                        onClick = {
                                            coroutineScope.launch {
                                                sheetState.hide()
                                            }
                                        },
                                        modifier = Modifier.weight(1f),
                                    )

                                    CareButtonMedium(
                                        text = stringResource(id = R.string.save),
                                        onClick = {
                                            coroutineScope.launch {
                                                val endTime = "${
                                                    if (localWorkEndAmPm == "오전" && localWorkEndHour == "12") "00"
                                                    else if (localWorkEndAmPm == "오전") localWorkEndHour
                                                    else if (localWorkEndAmPm == "오후" && localWorkEndHour == "12") "12"
                                                    else localWorkEndHour.toInt() + 12
                                                }" + ":${localWorkEndMinute}"
                                                Log.d("test", endTime.toString())
                                                onWorkEndTimeChanged(endTime)
                                                sheetState.hide()
                                            }
                                        },
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                            }

                            JobPostingBottomSheetType.POST_DEAD_LINE -> {
                                Text(
                                    text = stringResource(id = R.string.post_deadline),
                                    style = CareTheme.typography.heading3,
                                    color = CareTheme.colors.gray900,
                                )

                                CareCalendar(
                                    year = calendarDate.year,
                                    month = calendarDate.monthValue,
                                    selectedDate = applyDeadline,
                                    startMonth = startDateTime.monthValue,
                                    onMonthChanged = onCalendarMonthChanged,
                                    onDayClick = {
                                        coroutineScope.launch {
                                            onApplyDeadlineChanged(it)
                                            sheetState.hide()
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 30.dp),
                                )
                            }

                            else -> Unit
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Scaffold(
                    topBar = {
                        Column(
                            modifier = Modifier.padding(
                                start = 12.dp,
                                top = 48.dp,
                                end = 20.dp
                            )
                        ) {
                            val bottomPadding = if (jobPostingStep != JobPostingStep.SUMMARY) 12.dp
                            else 0.dp

                            CareSubtitleTopBar(
                                title = stringResource(id = R.string.post_job_posting),
                                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                                leftComponent = {
                                    if (jobPostingStep == JobPostingStep.SUMMARY) {
                                        CareButtonRound(
                                            text = stringResource(id = R.string.edit_job_posting_button),
                                            onClick = { setEditState(true) },
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = bottomPadding),
                            )

                            CareProgressBar(
                                currentStep = jobPostingStep.step,
                                totalSteps = JobPostingStep.entries.size - 1,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
                            )
                        }
                    },
                    containerColor = CareTheme.colors.white000,
                    modifier = Modifier.addFocusCleaner(focusManager),
                ) { paddingValue ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            32.dp,
                            Alignment.CenterVertically
                        ),
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp, top = 24.dp)
                            .padding(paddingValue),
                    ) {
                        CareStateAnimator(
                            targetState = jobPostingStep,
                            label = "센터 정보 입력을 관리하는 애니메이션",
                        ) { jobPostingStep ->
                            when (jobPostingStep) {
                                JobPostingStep.TIME_PAYMENT -> TimePaymentScreen(
                                    weekDays = weekDays,
                                    workStartTime = workStartTime,
                                    workEndTime = workEndTime,
                                    payType = payType,
                                    payAmount = payAmount,
                                    setWeekDays = setWeekDays,
                                    onPayTypeChanged = onPayTypeChanged,
                                    onPayAmountChanged = onPayAmountChanged,
                                    setJobPostingStep = setJobPostingStep,
                                    showBottomSheet = { sheetType ->
                                        coroutineScope.launch {
                                            setBottomSheetType(sheetType)
                                            sheetState.show()
                                        }
                                    },
                                )

                                JobPostingStep.ADDRESS -> AddressScreen(
                                    roadNameAddress = roadNameAddress,
                                    showPostCodeDialog = showPostCodeDialog,
                                    setJobPostingStep = setJobPostingStep,
                                )

                                JobPostingStep.CUSTOMER_INFORMATION -> CustomerInformationScreen(
                                    clientName = clientName,
                                    gender = gender,
                                    birthYear = birthYear,
                                    weight = weight,
                                    careLevel = careLevel,
                                    mentalStatus = mentalStatus,
                                    disease = disease,
                                    onClientNameChanged = onClientNameChanged,
                                    onGenderChanged = onGenderChanged,
                                    onBirthYearChanged = onBirthYearChanged,
                                    onWeightChanged = onWeightChanged,
                                    onCareLevelChanged = onCareLevelChanged,
                                    onMentalStatusChanged = onMentalStatusChanged,
                                    onDiseaseChanged = onDiseaseChanged,
                                    setJobPostingStep = setJobPostingStep,
                                )

                                JobPostingStep.CUSTOMER_REQUIREMENT -> CustomerRequirementScreen(
                                    isMealAssistance = isMealAssistance,
                                    isBowelAssistance = isBowelAssistance,
                                    isWalkingAssistance = isWalkingAssistance,
                                    lifeAssistance = lifeAssistance,
                                    extraRequirement = extraRequirement,
                                    onMealAssistanceChanged = onMealAssistanceChanged,
                                    onBowelAssistanceChanged = onBowelAssistanceChanged,
                                    onWalkingAssistanceChanged = onWalkingAssistanceChanged,
                                    onLifeAssistanceChanged = onLifeAssistanceChanged,
                                    onExtraRequirementChanged = onExtraRequirementChanged,
                                    setJobPostingStep = setJobPostingStep,
                                )

                                JobPostingStep.ADDITIONAL_INFO -> AdditionalInfoScreen(
                                    isExperiencePreferred = isExperiencePreferred,
                                    applyMethod = applyMethod,
                                    applyDeadlineType = applyDeadlineType,
                                    applyDeadline = applyDeadline,
                                    onExperiencePreferredChanged = onExperiencePreferredChanged,
                                    onApplyMethodChanged = onApplyMethodChanged,
                                    onApplyDeadlineTypeChanged = onApplyDeadlineTypeChanged,
                                    setJobPostingStep = setJobPostingStep,
                                    showBottomSheet = { sheetType ->
                                        coroutineScope.launch {
                                            setBottomSheetType(sheetType)
                                            sheetState.show()
                                        }
                                    }
                                )

                                JobPostingStep.SUMMARY -> Box(modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }
            }
        }
    }
}