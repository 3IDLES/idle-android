package com.idle.center.job.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.idle.compose.addFocusCleaner
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareChipBasic
import com.idle.designsystem.compose.component.CareChipShort
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.CareSubtitleTopAppBar
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.CareTextFieldLong
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JobEditScreen(
    weekDays: Set<DayOfWeek>,
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
    applyDeadline: String,
    setWeekDays: (DayOfWeek) -> Unit,
    clearWeekDays: () -> Unit,
    onPayTypeChanged: (PayType) -> Unit,
    onPayAmountChanged: (String) -> Unit,
    onDetailAddressChanged: (String) -> Unit,
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
    clearLifeAssistance: () -> Unit,
    onSpecialityChanged: (String) -> Unit,
    onExperiencePreferredChanged: (Boolean) -> Unit,
    onApplyMethodChanged: (ApplyMethod) -> Unit,
    clearApplyMethod: () -> Unit,
    onApplyDeadlineChanged: (String) -> Unit,
    onApplyDeadlineTypeChanged: (ApplyDeadlineType) -> Unit,
    setEditState: (Boolean) -> Unit,
) {
    var localWeekDays by remember { mutableStateOf(weekDays) }
    var localPayType by remember { mutableStateOf(payType) }
    var localPayAmount by remember { mutableStateOf(payAmount) }
    var localRoadNameAddress by remember { mutableStateOf(roadNameAddress) }
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

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    BackHandler { setEditState(false) }

    Scaffold(
        topBar = {
            CareSubtitleTopAppBar(
                title = "공고 수정",
                onNavigationClick = { setEditState(false) },
                leftComponent = {
                    Text(
                        text = "저장",
                        style = CareTheme.typography.subtitle2,
                        color = CareTheme.colors.orange500,
                        modifier = Modifier.clickable {
                            if(localWeekDays.isEmpty()){
                                return@clickable
                            }

                            if(applyMethod.isEmpty()){
                                return@clickable
                            }

                            if(clientName.isBlank()){
                                return@clickable
                            }

                            clearWeekDays()
                            localWeekDays.forEach { setWeekDays(it) }
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
                            onApplyDeadlineChanged(localApplyDeadline)
                            onApplyDeadlineTypeChanged(localApplyDeadlineType!!)
                            setEditState(false)
                        },
                    )
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, end = 20.dp),
            )
        },
        containerColor = CareTheme.colors.white000,
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                Text(
                    text = "근무 조건",
                    style = CareTheme.typography.subtitle1,
                    color = CareTheme.colors.gray900,
                )

                LabeledContent(
                    subtitle = "근무 요일",
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
                    subtitle = "근무 시간",
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        CareClickableTextField(
                            value = "",
                            hint = "시작 시간",
                            onClick = {},
                            leftComponent = {
                                Image(
                                    painter = painterResource(R.drawable.ic_arrow_down),
                                    contentDescription = null,
                                )
                            },
                            modifier = Modifier.weight(1f),
                        )

                        CareClickableTextField(
                            value = "",
                            hint = "종료 시간",
                            onClick = {},
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
                    subtitle = "급여",
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            CareChipBasic(
                                text = "시급",
                                onClick = { localPayType = PayType.HOURLY },
                                enable = localPayType == PayType.HOURLY,
                                modifier = Modifier.weight(1f),
                            )

                            CareChipBasic(
                                text = "주급",
                                onClick = { localPayType = PayType.WEEKLY },
                                enable = localPayType == PayType.WEEKLY,
                                modifier = Modifier.weight(1f),
                            )

                            CareChipBasic(
                                text = "월급",
                                onClick = { localPayType = PayType.MONTHLY },
                                enable = localPayType == PayType.MONTHLY,
                                modifier = Modifier.weight(1f),
                            )
                        }

                        CareTextField(
                            value = localPayAmount,
                            hint = "급여를 입력하세요",
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
                        subtitle = "도로명 주소",
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        CareClickableTextField(
                            value = localRoadNameAddress,
                            hint = "도로명 주소를 입력해주세요.",
                            onClick = showPostCodeDialog,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    LabeledContent(
                        subtitle = "상세 주소",
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        CareTextField(
                            value = localDetailAddress,
                            hint = "상세 주소를 입력해주세요. (예: 2층 204호)",
                            onValueChanged = { localDetailAddress = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "고객 정보",
                    style = CareTheme.typography.subtitle1,
                    color = CareTheme.colors.gray900,
                )

                LabeledContent(
                    subtitle = "고객 이름",
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    CareTextField(
                        value = localClientName,
                        onValueChanged = { localClientName = it },
                        hint = "고객 이름을 입력해주세요.",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                LabeledContent(
                    subtitle = "성별",
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
                    subtitle = "출생연도",
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    CareTextField(
                        value = localBirthYear,
                        onValueChanged = { localBirthYear = it },
                        keyboardType = KeyboardType.Number,
                        hint = "고객의 출생연도를 입력해주세요. (예: 1965)",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                LabeledContent(
                    subtitle = "몸무게",
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    CareTextField(
                        value = localWeight,
                        onValueChanged = { localWeight = it },
                        keyboardType = KeyboardType.Number,
                        hint = "고객의 몸무게를 입력해주세요. (예: 60)",
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
                    subtitle = "요양 등급",
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
                    subtitle = "인지 상태",
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
                        hint = "고객이 현재 앓고 있는 질병 또는 병력을 입력해주세요.",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

                LabeledContent(
                    subtitle = "식사보조"
                ) {
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

                LabeledContent(
                    subtitle = "배변보조"
                ) {
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

                LabeledContent(
                    subtitle = "이동보조"
                ) {
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
                                    localLifeAssistance = if (assistance in localLifeAssistance) {
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
                        hint = "추가적으로 요구사항이 있다면 작성해주세요.(예: 어쩌고저쩌고)",
                        onValueChanged = { localSpeciality = it },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 28.dp)
            ) {
                Text(
                    text = "추가 지원 정보",
                    style = CareTheme.typography.subtitle1,
                    color = CareTheme.colors.gray900,
                )

                LabeledContent(
                    subtitle = "경력 우대 여부"
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CareChipBasic(
                            text = "초보 가능",
                            onClick = { localIsExperiencePreferred = false },
                            enable = localIsExperiencePreferred == false,
                            modifier = Modifier.width(104.dp),
                        )

                        CareChipBasic(
                            text = "경력 우대",
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
                    subtitle = "접수 마감일",
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

                        if (localApplyDeadlineType == ApplyDeadlineType.DEFINITE) {
                            CareTextField(
                                value = localApplyDeadline,
                                onValueChanged = { localApplyDeadline = it },
                                hint = "날짜를 선택해주세요.",
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