@file:OptIn(ExperimentalMaterialApi::class)

package com.idle.center.jobposting

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.idle.analytics.helper.LocalAnalyticsHelper
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.CareBaseEvent
import com.idle.center.job.edit.JobEditScreen
import com.idle.center.jobposting.JobPostingStep.ADDRESS
import com.idle.center.jobposting.JobPostingStep.SUMMARY
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
import com.idle.designsystem.compose.component.CareCalendar
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareWheelPicker
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.ApplyDeadlineType
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.DayOfWeek
import com.idle.domain.model.jobposting.LifeAssistance
import com.idle.domain.model.jobposting.MentalStatus
import com.idle.domain.model.jobposting.PayType
import com.idle.domain.model.profile.CenterProfile
import com.idle.post.code.PostCodeFragment
import com.idle.worker.job.posting.detail.center.JobPostingPreviewScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
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
            val profile by profile.collectAsStateWithLifecycle()

            val postCodeDialog: PostCodeFragment? by lazy {
                PostCodeFragment().apply {
                    onDismissCallback = {
                        findNavController().currentBackStackEntry?.savedStateHandle?.let {
                            val roadName = it.get<String>("roadNameAddress")
                            val lotNumber = it.get<String>("lotNumberAddress")

                            fragmentViewModel.setRoadNameAddress(roadName ?: "")
                            fragmentViewModel.setLotNumberAddress(lotNumber ?: "")

                            if (jobPostingStep == ADDRESS) {
                                setJobPostingStep(JobPostingStep.findStep(ADDRESS.step + 1))
                            }
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
                        snackbarHostState = snackbarHostState,
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
                        showSnackBar = { baseEvent(CareBaseEvent.ShowSnackBar(it)) },
                    )
                } else {
                    JobPostingScreen(
                        snackbarHostState = snackbarHostState,
                        profile = profile,
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
                        setJobPostingStep = { step ->
                            snackbarHostState.currentSnackbarData?.dismiss()
                            setJobPostingStep(step)
                        },
                        setEditState = ::setEditState,
                        setBottomSheetType = ::setBottomSheetType,
                        showSnackBar = { baseEvent(CareBaseEvent.ShowSnackBar(it)) },
                        navigateToHome = {
                            baseEvent(
                                CareBaseEvent.NavigateTo(
                                    DeepLinkDestination.CenterHome,
                                    com.idle.center.job.posting.post.R.id.jobPostingPostFragment
                                )
                            )
                        },
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
internal fun JobPostingScreen(
    snackbarHostState: SnackbarHostState,
    profile: CenterProfile?,
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
    showSnackBar: (String) -> Unit,
    navigateToHome: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val startDateTime by rememberSaveable { mutableStateOf(calendarDate) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    val analyticsHelper = LocalAnalyticsHelper.current

    AnimatedContent(
        targetState = jobPostingStep.topBarStep,
        transitionSpec = {
            if (targetState > initialState) {
                slideInHorizontally(initialOffsetX = { it }) + fadeIn() togetherWith
                        slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
            } else {
                slideInHorizontally(initialOffsetX = { -it }) + fadeIn() togetherWith
                        slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            }
        },
    ) { step ->
        when (step) {
            1 -> JobPostingSummaryScreen(
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

            2 -> JobPostingPreviewScreen(
                weekdays = weekDays,
                workStartTime = workStartTime,
                workEndTime = workEndTime,
                payType = payType ?: PayType.HOURLY,
                payAmount = payAmount,
                lotNumberAddress = lotNumberAddress,
                gender = gender,
                birthYear = birthYear,
                weight = weight,
                careLevel = careLevel,
                mentalStatus = mentalStatus,
                disease = disease,
                isMealAssistance = isMealAssistance ?: true,
                isBowelAssistance = isBowelAssistance ?: true,
                isWalkingAssistance = isWalkingAssistance ?: true,
                lifeAssistance = lifeAssistance,
                extraRequirement = extraRequirement,
                isExperiencePreferred = isExperiencePreferred ?: true,
                applyMethod = applyMethod,
                applyDeadline = applyDeadline,
                centerProfile = profile,
                onBackPressed = { setJobPostingStep(SUMMARY) },
            )

            0 -> {
                CareBottomSheetLayout(
                    sheetState = sheetState,
                    sheetContent = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            var actionStartTime by remember { mutableStateOf(System.currentTimeMillis()) }

                            when (bottomSheetType) {
                                JobPostingBottomSheetType.WORK_START_TIME -> {
                                    var localWorkStartAmPm by remember { mutableStateOf("오전") }
                                    var localWorkStartHour by remember { mutableStateOf("01") }
                                    var localWorkStartMinute by remember { mutableStateOf("00") }

                                    Text(
                                        text = stringResource(id = R.string.work_start_time),
                                        style = CareTheme.typography.heading3,
                                        color = CareTheme.colors.black,
                                    )

                                    Spacer(modifier = Modifier.height(80.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CareWheelPicker(
                                            items = listOf("오전", "오후"),
                                            onItemSelected = { localWorkStartAmPm = it },
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
                                            color = CareTheme.colors.black,
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
                                            text = stringResource(id = R.string.cancel_short),
                                            border = BorderStroke(
                                                width = 1.dp,
                                                color = CareTheme.colors.orange400
                                            ),
                                            containerColor = CareTheme.colors.white000,
                                            textColor = CareTheme.colors.orange500,
                                            onClick = {
                                                coroutineScope.launch {
                                                    sheetState.hide()
                                                    val actionEndTime = System.currentTimeMillis()
                                                    analyticsHelper.logActionDuration(
                                                        screenName = "job_posting_screen",
                                                        actionName = "work_start_time",
                                                        isSuccess = false,
                                                        timeMillis = (actionEndTime - actionStartTime)
                                                    )
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

                                                    val actionEndTime = System.currentTimeMillis()
                                                    analyticsHelper.logActionDuration(
                                                        screenName = "job_posting_screen",
                                                        actionName = "work_start_time",
                                                        isSuccess = true,
                                                        timeMillis = (actionEndTime - actionStartTime)
                                                    )
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
                                        color = CareTheme.colors.black,
                                    )

                                    Spacer(modifier = Modifier.height(80.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CareWheelPicker(
                                            items = listOf("오전", "오후"),
                                            onItemSelected = { localWorkEndAmPm = it },
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
                                            color = CareTheme.colors.black,
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
                                            text = stringResource(id = R.string.cancel_short),
                                            border = BorderStroke(
                                                width = 1.dp,
                                                color = CareTheme.colors.orange400
                                            ),
                                            containerColor = CareTheme.colors.white000,
                                            textColor = CareTheme.colors.orange500,
                                            onClick = {
                                                coroutineScope.launch {
                                                    sheetState.hide()

                                                    val actionEndTime = System.currentTimeMillis()
                                                    analyticsHelper.logActionDuration(
                                                        screenName = "job_posting_screen",
                                                        actionName = "work_end_time",
                                                        isSuccess = false,
                                                        timeMillis = (actionEndTime - actionStartTime)
                                                    )
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

                                                    onWorkEndTimeChanged(endTime)
                                                    sheetState.hide()

                                                    val actionEndTime = System.currentTimeMillis()
                                                    analyticsHelper.logActionDuration(
                                                        screenName = "job_posting_screen",
                                                        actionName = "work_end_time",
                                                        isSuccess = true,
                                                        timeMillis = (actionEndTime - actionStartTime)
                                                    )
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
                                        color = CareTheme.colors.black,
                                    )

                                    CareCalendar(
                                        year = calendarDate.year,
                                        month = calendarDate.monthValue,
                                        selectedDate = applyDeadline,
                                        startDate = startDateTime,
                                        onMonthChanged = onCalendarMonthChanged,
                                        onDayClick = {
                                            coroutineScope.launch {
                                                onApplyDeadlineChanged(it)
                                                sheetState.hide()

                                                val actionEndTime = System.currentTimeMillis()
                                                analyticsHelper.logActionDuration(
                                                    screenName = "job_posting_screen",
                                                    actionName = "post_dead_line",
                                                    isSuccess = true,
                                                    timeMillis = (actionEndTime - actionStartTime)
                                                )
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
                    modifier = Modifier.fillMaxSize(),
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
                                CareSubtitleTopBar(
                                    title = stringResource(id = R.string.post_job_posting),
                                    onNavigationClick = navigateToHome,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
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
                        snackbarHost = {
                            SnackbarHost(
                                hostState = snackbarHostState,
                                snackbar = { data ->
                                    CareSnackBar(
                                        data = data,
                                        modifier = Modifier.padding(bottom = 116.dp)
                                    )
                                }
                            )
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
                                        showSnackBar = showSnackBar,
                                    )

                                    ADDRESS -> AddressScreen(
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
                                        showSnackBar = showSnackBar,
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

                                    else -> Box(modifier = Modifier.fillMaxSize())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}