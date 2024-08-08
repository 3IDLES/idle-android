@file:OptIn(ExperimentalMaterialApi::class)

package com.idle.center.job.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.idle.compose.JobPostingBottomSheetType
import com.idle.compose.JobPostingBottomSheetType.POST_DEAD_LINE
import com.idle.compose.JobPostingBottomSheetType.WORK_END_TIME
import com.idle.compose.JobPostingBottomSheetType.WORK_START_TIME
import com.idle.compose.addFocusCleaner
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareBottomSheetLayout
import com.idle.designsystem.compose.component.CareButtonMedium
import com.idle.designsystem.compose.component.CareCalendar
import com.idle.designsystem.compose.component.CareChipBasic
import com.idle.designsystem.compose.component.CareChipShort
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.CareSubtitleTopAppBar
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.component.CareWheelPicker
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.designsystem.compose.foundation.PretendardMedium
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyDeadlineType
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.LifeAssistance
import com.idle.domain.model.job.MentalStatus
import com.idle.domain.model.job.PayType
import com.idle.post.code.PostCodeFragment
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JobEditScreen(
    weekDays: Set<DayOfWeek>,
    workStartTime: String,
    workEndTime: String,
    fragmentManager: FragmentManager,
    payType: PayType?,
    payAmount: String,
    roadNameAddress: String,
    detailAddress: String,
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
    speciality: String,
    isExperiencePreferred: Boolean?,
    applyMethod: Set<ApplyMethod>,
    applyDeadlineType: ApplyDeadlineType?,
    applyDeadline: LocalDate?,
    calendarDate: LocalDate,
    setWeekDays: (DayOfWeek) -> Unit,
    clearWeekDays: () -> Unit,
    onWorkStartTimeChanged: (String) -> Unit,
    onWorkEndTimeChanged: (String) -> Unit,
    onPayTypeChanged: (PayType) -> Unit,
    onPayAmountChanged: (String) -> Unit,
    onRoadNameAddressChanged: (String) -> Unit,
    onLotNumberAddressChanged: (String) -> Unit,
    onDetailAddressChanged: (String) -> Unit,
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
    clearLifeAssistance: () -> Unit,
    onSpecialityChanged: (String) -> Unit,
    onExperiencePreferredChanged: (Boolean) -> Unit,
    onApplyMethodChanged: (ApplyMethod) -> Unit,
    clearApplyMethod: () -> Unit,
    onApplyDeadlineChanged: (LocalDate) -> Unit,
    onCalendarMonthChanged: (Int) -> Unit,
    onApplyDeadlineTypeChanged: (ApplyDeadlineType) -> Unit,
    setEditState: (Boolean) -> Unit,
) {
    var localWeekDays by remember { mutableStateOf(weekDays) }
    var localWorkStartTime by remember { mutableStateOf(workStartTime) }
    var localWorkEndTime by remember { mutableStateOf(workEndTime) }
    var localPayType by remember { mutableStateOf(payType) }
    var localPayAmount by remember { mutableStateOf(payAmount) }
    var localRoadNameAddress by remember { mutableStateOf(roadNameAddress) }
    var localLotNumberAddress by remember { mutableStateOf("") }
    var localDetailAddress by remember { mutableStateOf(detailAddress) }
    var localClientName by remember { mutableStateOf(clientName) }
    var localGender by remember { mutableStateOf(gender) }
    var localBirthYear by remember { mutableStateOf(birthYear) }
    var localWeight by remember { mutableStateOf(weight) }
    var localCareLevel by remember { mutableStateOf(careLevel) }
    var localMentalStatus by remember { mutableStateOf(mentalStatus) }
    var localDisease by remember { mutableStateOf(disease) }
    var localIsMealAssistance by remember { mutableStateOf(isMealAssistance) }
    var localIsBowelAssistance by remember { mutableStateOf(isBowelAssistance) }
    var localIsWalkingAssistance by remember { mutableStateOf(isWalkingAssistance) }
    var localLifeAssistance by remember { mutableStateOf(lifeAssistance) }
    var localSpeciality by remember { mutableStateOf(speciality) }
    var localIsExperiencePreferred by remember { mutableStateOf(isExperiencePreferred) }
    var localApplyMethod by remember { mutableStateOf(applyMethod) }
    var localApplyDeadlineType by remember { mutableStateOf(applyDeadlineType) }
    var localApplyDeadline by remember { mutableStateOf(applyDeadline) }
    val startDate by rememberSaveable { mutableStateOf(calendarDate) }

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    var bottomSheetType: JobPostingBottomSheetType? by rememberSaveable { mutableStateOf(null) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )

    val postCodeDialog: PostCodeFragment? by lazy {
        PostCodeFragment().apply {
            onDismissCallback = {
                findNavController().currentBackStackEntry?.savedStateHandle?.let {
                    val roadNameAddress = it.get<String>("roadNameAddress")
                    val lotNumberAddress = it.get<String>("lotNumberAddress")

                    localRoadNameAddress = roadNameAddress ?: ""
                    localLotNumberAddress = lotNumberAddress ?: ""
                }
            }
        }
    }


    BackHandler { setEditState(false) }

    CareBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                when (bottomSheetType) {
                    WORK_START_TIME -> {
                        var tempWorkStartAmPm by remember { mutableStateOf("오전") }
                        var tempWorkStartHour by remember { mutableStateOf("01") }
                        var tempWorkStartMinute by remember { mutableStateOf("00") }

                        Text(
                            text = stringResource(id = R.string.work_start_time),
                            style = CareTheme.typography.heading3,
                            color = CareTheme.colors.gray900,
                        )

                        Spacer(modifier = Modifier.height(80.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CareWheelPicker(
                                items = listOf("오전", "오후"),
                                onItemSelected = { tempWorkStartAmPm = it },
                                initIndex = if (
                                    localWorkStartTime.isBlank() ||
                                    localWorkStartTime.substring(0, 2).toInt() <= 12
                                ) 0 else 1,
                                modifier = Modifier.padding(end = 40.dp),
                            )

                            CareWheelPicker(
                                items = (1..12).toList(),
                                onItemSelected = { tempWorkStartHour = it },
                                initIndex = if (localWorkStartTime.isBlank()) 0
                                else if (localWorkStartTime.substring(0, 2) == "00") 11
                                else localWorkStartTime.substring(0, 2).toInt() - 1,
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
                                onItemSelected = { tempWorkStartMinute = it },
                                initIndex = if (localWorkStartTime.isBlank()) 0
                                else (localWorkStartTime.substring(3, 5).toInt() / 10),
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
                                                if (tempWorkStartAmPm == "오전" && tempWorkStartHour == "12") "00"
                                                else if (tempWorkStartAmPm == "오전") tempWorkStartHour
                                                else if (tempWorkStartAmPm == "오후" && tempWorkStartHour == "12") "12"
                                                else tempWorkStartHour.toInt() + 12
                                            }" + ":${tempWorkStartMinute}"
                                        localWorkStartTime = startTime
                                        sheetState.hide()
                                    }
                                },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }

                    WORK_END_TIME -> {
                        var tempWorkEndAmPm by remember { mutableStateOf("오전") }
                        var tempWorkEndHour by remember { mutableStateOf("01") }
                        var tempWorkEndMinute by remember { mutableStateOf("00") }

                        Text(
                            text = stringResource(id = R.string.work_end_time),
                            style = CareTheme.typography.heading3,
                            color = CareTheme.colors.gray900,
                        )

                        Spacer(modifier = Modifier.height(80.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CareWheelPicker(
                                items = listOf("오전", "오후"),
                                onItemSelected = { tempWorkEndAmPm = it },
                                initIndex = if (
                                    localWorkEndTime.isBlank() ||
                                    localWorkEndTime.substring(0, 2).toInt() <= 12
                                ) 0 else 1,
                                modifier = Modifier.padding(end = 40.dp),
                            )

                            CareWheelPicker(
                                items = (1..12).toList(),
                                onItemSelected = { tempWorkEndHour = it },
                                initIndex = if (localWorkEndTime.isBlank()) 0
                                else if (localWorkEndTime.substring(0, 2) == "00") 11
                                else localWorkEndTime.substring(0, 2).toInt() - 1,
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
                                onItemSelected = { tempWorkEndMinute = it },
                                initIndex = if (localWorkEndTime.isBlank()) 0
                                else localWorkEndTime.substring(3, 5).toInt() / 10,
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
                                            if (tempWorkEndAmPm == "오전" && tempWorkEndHour == "12") "00"
                                            else if (tempWorkEndAmPm == "오전") tempWorkEndHour
                                            else if (tempWorkEndAmPm == "오후" && tempWorkEndHour == "12") "12"
                                            else tempWorkEndHour.toInt() + 12
                                        }" + ":${tempWorkEndMinute}"
                                        localWorkEndTime = endTime
                                        sheetState.hide()
                                    }
                                },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }

                    POST_DEAD_LINE -> {
                        Text(
                            text = stringResource(id = R.string.post_deadline),
                            style = CareTheme.typography.heading3,
                            color = CareTheme.colors.gray900,
                        )

                        CareCalendar(
                            year = calendarDate.year,
                            month = calendarDate.monthValue,
                            selectedDate = applyDeadline,
                            startMonth = startDate.monthValue,
                            onMonthChanged = onCalendarMonthChanged,
                            onDayClick = {
                                coroutineScope.launch {
                                    localApplyDeadline = it
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
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                CareSubtitleTopAppBar(
                    title = stringResource(id = R.string.edit_job_posting),
                    onNavigationClick = { setEditState(false) },
                    leftComponent = {
                        Text(
                            text = stringResource(id = R.string.save),
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.orange500,
                            modifier = Modifier.clickable(
                                enabled = localWeekDays.isNotEmpty() &&
                                        applyMethod.isNotEmpty() &&
                                        clientName.isNotBlank()
                            ) {
                                clearWeekDays()
                                localWeekDays.forEach { setWeekDays(it) }
                                onWorkStartTimeChanged(localWorkStartTime)
                                onWorkEndTimeChanged(localWorkEndTime)
                                onPayTypeChanged(localPayType!!)
                                onPayAmountChanged(localPayAmount)
                                onDetailAddressChanged(localDetailAddress)
                                onClientNameChanged(localClientName)
                                onGenderChanged(localGender)
                                onBirthYearChanged(localBirthYear)
                                onWeightChanged(localWeight)
                                onCareLevelChanged(localCareLevel)
                                onMentalStatusChanged(localMentalStatus)
                                onDiseaseChanged(localDisease)
                                onMealAssistanceChanged(localIsMealAssistance!!)
                                onBowelAssistanceChanged(localIsBowelAssistance!!)
                                onWalkingAssistanceChanged(localIsWalkingAssistance!!)
                                clearLifeAssistance()
                                localLifeAssistance.forEach { onLifeAssistanceChanged(it) }
                                onSpecialityChanged(localSpeciality)
                                onExperiencePreferredChanged(localIsExperiencePreferred!!)
                                clearApplyMethod()
                                localApplyMethod.forEach { onApplyMethodChanged(it) }
                                onApplyDeadlineChanged(localApplyDeadline!!)
                                onApplyDeadlineTypeChanged(localApplyDeadlineType!!)
                                onRoadNameAddressChanged(localRoadNameAddress)
                                onLotNumberAddressChanged(localLotNumberAddress)
                                setEditState(false)
                            },
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 48.dp, end = 20.dp),
                )
            },
            containerColor = CareTheme.colors.white000,
            modifier = Modifier.addFocusCleaner(focusManager),
        ) { paddingValue ->
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValue)
                    .verticalScroll(scrollState)
            ) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = CareTheme.colors.gray100,
                    modifier = Modifier.padding(top = 24.dp)
                )

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.work_conditions),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.gray900,
                    )

                    LabeledContent(
                        subtitle = stringResource(id = R.string.work_days),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            DayOfWeek.entries.forEach { day ->
                                CareChipShort(
                                    text = day.displayName,
                                    enable = day in localWeekDays,
                                    onClick = {
                                        localWeekDays = if (day in localWeekDays) {
                                            localWeekDays - day
                                        } else {
                                            localWeekDays + day
                                        }
                                    },
                                )
                            }
                        }
                    }


                    LabeledContent(
                        subtitle = stringResource(id = R.string.work_hours),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            CareClickableTextField(
                                value = localWorkStartTime,
                                hint = stringResource(id = R.string.work_start_time_hint),
                                onClick = {
                                    coroutineScope.launch {
                                        bottomSheetType = WORK_START_TIME
                                        sheetState.show()
                                    }
                                },
                                leftComponent = {
                                    Image(
                                        painter = painterResource(R.drawable.ic_arrow_down),
                                        contentDescription = null,
                                    )
                                },
                                modifier = Modifier.weight(1f),
                            )

                            CareClickableTextField(
                                value = localWorkEndTime,
                                hint = stringResource(id = R.string.work_end_time_hint),
                                onClick = {
                                    coroutineScope.launch {
                                        bottomSheetType = WORK_END_TIME
                                        sheetState.show()
                                    }
                                },
                                leftComponent = {
                                    Image(
                                        painter = painterResource(R.drawable.ic_arrow_down),
                                        contentDescription = null,
                                    )
                                },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }

                    LabeledContent(
                        subtitle = stringResource(id = R.string.pay),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                CareChipBasic(
                                    text = stringResource(id = R.string.hourly),
                                    onClick = { localPayType = PayType.HOURLY },
                                    enable = localPayType == PayType.HOURLY,
                                    modifier = Modifier.weight(1f),
                                )

                                CareChipBasic(
                                    text = stringResource(id = R.string.weekly),
                                    onClick = { localPayType = PayType.WEEKLY },
                                    enable = localPayType == PayType.WEEKLY,
                                    modifier = Modifier.weight(1f),
                                )

                                CareChipBasic(
                                    text = stringResource(id = R.string.monthly),
                                    onClick = { localPayType = PayType.MONTHLY },
                                    enable = localPayType == PayType.MONTHLY,
                                    modifier = Modifier.weight(1f),
                                )
                            }

                            CareTextField(
                                value = localPayAmount,
                                hint = stringResource(id = R.string.pay_amount_hint),
                                textStyle = CareTheme.typography.body2,
                                onValueChanged = { localPayAmount = it },
                                keyboardType = KeyboardType.Number,
                                leftComponent = {
                                    Text(
                                        text = "원",
                                        style = CareTheme.typography.body3,
                                        color = CareTheme.colors.gray500,
                                    )
                                },
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        LabeledContent(
                            subtitle = stringResource(id = R.string.road_name_address),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            CareClickableTextField(
                                value = localRoadNameAddress,
                                hint = stringResource(id = R.string.road_name_address_hint),
                                onClick = {
                                    if (!(postCodeDialog?.isAdded == true || postCodeDialog?.isVisible == true)) {
                                        postCodeDialog?.show(fragmentManager, "PostCodeFragment")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }

                        LabeledContent(
                            subtitle = stringResource(id = R.string.detail_address),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            CareTextField(
                                value = localDetailAddress,
                                hint = stringResource(id = R.string.detail_address_hint),
                                onValueChanged = { localDetailAddress = it },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.customer_info),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.gray900,
                    )

                    LabeledContent(
                        subtitle = stringResource(id = R.string.customer_name),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        CareTextField(
                            value = localClientName,
                            onValueChanged = { localClientName = it },
                            hint = stringResource(id = R.string.customer_name_hint),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    LabeledContent(
                        subtitle = stringResource(id = R.string.gender),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CareChipBasic(
                                text = Gender.WOMAN.displayName,
                                onClick = { localGender = Gender.WOMAN },
                                enable = localGender == Gender.WOMAN,
                                modifier = Modifier.width(104.dp),
                            )

                            CareChipBasic(
                                text = Gender.MAN.displayName,
                                onClick = { localGender = Gender.MAN },
                                enable = localGender == Gender.MAN,
                                modifier = Modifier.width(104.dp),
                            )
                        }
                    }

                    LabeledContent(
                        subtitle = stringResource(id = R.string.birth_year),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        CareTextField(
                            value = localBirthYear,
                            onValueChanged = { localBirthYear = it },
                            keyboardType = KeyboardType.Number,
                            hint = stringResource(id = R.string.birth_year_hint),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    LabeledContent(
                        subtitle = stringResource(id = R.string.weight),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        CareTextField(
                            value = localWeight,
                            onValueChanged = { localWeight = it },
                            keyboardType = KeyboardType.Number,
                            hint = stringResource(id = R.string.weight_hint),
                            leftComponent = {
                                Text(
                                    text = "kg",
                                    style = CareTheme.typography.body3,
                                    color = CareTheme.colors.gray500,
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    LabeledContent(
                        subtitle = stringResource(id = R.string.care_level),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IntRange(1, 5).forEach { level ->
                                CareChipShort(
                                    text = level.toString(),
                                    enable = localCareLevel == level.toString(),
                                    onClick = { localCareLevel = level.toString() },
                                )
                            }
                        }
                    }

                    LabeledContent(
                        subtitle = stringResource(id = R.string.mental_status),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            MentalStatus.entries.forEach { status ->
                                if (status == MentalStatus.UNKNOWN) {
                                    return@forEach
                                }

                                CareChipBasic(
                                    text = status.displayName,
                                    enable = localMentalStatus == status,
                                    onClick = { localMentalStatus = status },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }

                    LabeledContent(
                        subtitle = buildAnnotatedString {
                            append("질병 ")
                            withStyle(
                                style = SpanStyle(
                                    color = CareTheme.colors.gray300,
                                    fontSize = CareTheme.typography.caption.fontSize,
                                    fontFamily = PretendardMedium,
                                )
                            ) {
                                append("(선택)")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        CareTextField(
                            value = localDisease,
                            onValueChanged = { localDisease = it },
                            hint = stringResource(id = R.string.disease_hint),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

                    LabeledContent(subtitle = stringResource(id = R.string.meal_assistance)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CareChipBasic(
                                text = stringResource(R.string.necessary),
                                onClick = { localIsMealAssistance = true },
                                enable = localIsMealAssistance == true,
                                modifier = Modifier.width(104.dp),
                            )

                            CareChipBasic(
                                text = stringResource(R.string.unnecessary),
                                onClick = { localIsMealAssistance = false },
                                enable = localIsMealAssistance == false,
                                modifier = Modifier.width(104.dp),
                            )
                        }
                    }

                    LabeledContent(subtitle = stringResource(id = R.string.bowel_assistance)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CareChipBasic(
                                text = stringResource(R.string.necessary),
                                onClick = { localIsBowelAssistance = true },
                                enable = localIsBowelAssistance == true,
                                modifier = Modifier.width(104.dp),
                            )

                            CareChipBasic(
                                text = stringResource(R.string.unnecessary),
                                onClick = { localIsBowelAssistance = false },
                                enable = localIsBowelAssistance == false,
                                modifier = Modifier.width(104.dp),
                            )
                        }
                    }

                    LabeledContent(subtitle = stringResource(id = R.string.walking_assistance)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CareChipBasic(
                                text = stringResource(R.string.necessary),
                                onClick = { localIsWalkingAssistance = true },
                                enable = localIsWalkingAssistance == true,
                                modifier = Modifier.width(104.dp),
                            )

                            CareChipBasic(
                                text = stringResource(R.string.unnecessary),
                                onClick = { localIsWalkingAssistance = false },
                                enable = localIsWalkingAssistance == false,
                                modifier = Modifier.width(104.dp),
                            )
                        }
                    }

                    LabeledContent(
                        subtitle = buildAnnotatedString {
                            append("일상보조 ")
                            withStyle(
                                style = SpanStyle(
                                    color = CareTheme.colors.gray300,
                                    fontSize = CareTheme.typography.caption.fontSize,
                                    fontFamily = PretendardMedium,
                                )
                            ) {
                                append("(선택)")
                            }
                        },
                    ) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            maxItemsInEachRow = 3,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LifeAssistance.entries.forEach { assistance ->
                                CareChipBasic(
                                    text = assistance.displayName,
                                    onClick = {
                                        localLifeAssistance =
                                            if (assistance in localLifeAssistance) {
                                                localLifeAssistance - assistance
                                            } else {
                                                localLifeAssistance + assistance
                                            }
                                    },
                                    enable = assistance in localLifeAssistance,
                                    modifier = Modifier.width(104.dp),
                                )
                            }
                        }
                    }

                    LabeledContent(
                        subtitle = buildAnnotatedString {
                            append("특이사항")
                            withStyle(
                                style = SpanStyle(
                                    color = CareTheme.colors.gray300,
                                    fontSize = CareTheme.typography.caption.fontSize,
                                    fontFamily = PretendardMedium,
                                )
                            ) {
                                append("(선택)")
                            }
                        },
                    ) {
                        CareTextFieldLong(
                            value = localSpeciality,
                            hint = stringResource(id = R.string.speciality_hint),
                            onValueChanged = { localSpeciality = it },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 28.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.additional_info),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.gray900,
                    )

                    LabeledContent(subtitle = stringResource(id = R.string.experience_preference)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CareChipBasic(
                                text = stringResource(id = R.string.beginner_possible),
                                onClick = { localIsExperiencePreferred = false },
                                enable = localIsExperiencePreferred == false,
                                modifier = Modifier.width(104.dp),
                            )

                            CareChipBasic(
                                text = stringResource(id = R.string.experience_preferred),
                                onClick = { localIsExperiencePreferred = true },
                                enable = localIsExperiencePreferred == true,
                                modifier = Modifier.width(104.dp),
                            )
                        }
                    }

                    LabeledContent(
                        subtitle = buildAnnotatedString {
                            append("지원 방법 ")
                            withStyle(
                                style = SpanStyle(
                                    color = CareTheme.colors.gray300,
                                    fontSize = CareTheme.typography.caption.fontSize,
                                    fontFamily = PretendardMedium,
                                )
                            ) {
                                append("(다중 선택 가능)")
                            }
                        },
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            ApplyMethod.entries.forEach { method ->
                                CareChipBasic(
                                    text = method.displayName,
                                    onClick = {
                                        localApplyMethod = if (method in localApplyMethod) {
                                            localApplyMethod - method
                                        } else {
                                            localApplyMethod + method
                                        }
                                    },
                                    enable = method in localApplyMethod,
                                    modifier = Modifier.width(104.dp),
                                )
                            }
                        }
                    }

                    LabeledContent(
                        subtitle = stringResource(id = R.string.apply_deadline),
                        modifier = Modifier.padding(bottom = 68.dp),
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                ApplyDeadlineType.entries.forEach { type ->
                                    CareChipBasic(
                                        text = type.displayName,
                                        onClick = { localApplyDeadlineType = type },
                                        enable = localApplyDeadlineType == type,
                                        modifier = Modifier.width(104.dp),
                                    )
                                }
                            }

                            if (localApplyDeadlineType == ApplyDeadlineType.LIMITED) {
                                CareClickableTextField(
                                    value = localApplyDeadline?.toString() ?: "",
                                    onClick = {
                                        coroutineScope.launch {
                                            bottomSheetType = POST_DEAD_LINE
                                            sheetState.show()
                                        }
                                    },
                                    hint = stringResource(id = R.string.apply_deadline_hint),
                                    leftComponent = {
                                        Image(
                                            painter = painterResource(com.idle.designresource.R.drawable.ic_calendar),
                                            contentDescription = null,
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}