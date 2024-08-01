package com.idle.center.job.edit

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareChipBasic
import com.idle.designsystem.compose.component.CareChipShort
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.designsystem.compose.foundation.PretendardMedium
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyDeadlineChipState
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
    applyDeadlineChipState: ApplyDeadlineChipState?,
    applyDeadline: String,
    setWeekDays: (DayOfWeek) -> Unit,
    onPayTypeChanged: (PayType) -> Unit,
    onPayAmountChanged: (String) -> Unit,
    onDetailAddressChanged: (String) -> Unit,
    showPostCodeDialog: () -> Unit,
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
    onSpecialityChanged: (String) -> Unit,
    onExperiencePreferredChanged: (Boolean) -> Unit,
    onApplyMethodChanged: (ApplyMethod) -> Unit,
    onApplyDeadlineChanged: (String) -> Unit,
    onApplyDeadlineChipStateChanged: (ApplyDeadlineChipState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(start = 20.dp, end = 20.dp, top = 24.dp),
    ) {
        HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

        Text(
            text = "근무 조건",
            style = CareTheme.typography.subtitle1,
            color = CareTheme.colors.gray900,
            modifier = Modifier.padding(bottom = 12.dp, top = 20.dp),
        )

        LabeledContent(
            subtitle = "근무 요일",
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                DayOfWeek.entries.forEach { day ->
                    CareChipShort(
                        text = day.displayName,
                        enable = day in weekDays,
                        onClick = { setWeekDays(day) },
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
                        onClick = { onPayTypeChanged(PayType.HOURLY) },
                        enable = payType == PayType.HOURLY,
                        modifier = Modifier.weight(1f),
                    )

                    CareChipBasic(
                        text = "주급",
                        onClick = { onPayTypeChanged(PayType.WEEKLY) },
                        enable = payType == PayType.WEEKLY,
                        modifier = Modifier.weight(1f),
                    )

                    CareChipBasic(
                        text = "월급",
                        onClick = { onPayTypeChanged(PayType.MONTHLY) },
                        enable = payType == PayType.MONTHLY,
                        modifier = Modifier.weight(1f),
                    )
                }

                CareTextField(
                    value = payAmount,
                    hint = "급여를 입력하세요",
                    textStyle = CareTheme.typography.body2,
                    onValueChanged = onPayAmountChanged,
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

        LabeledContent(
            subtitle = "도로명 주소",
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareClickableTextField(
                value = roadNameAddress,
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
                value = detailAddress,
                hint = "상세 주소를 입력해주세요. (예: 2층 204호)",
                onValueChanged = onDetailAddressChanged,
                modifier = Modifier.fillMaxWidth()
            )
        }

        HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "고객 정보",
                style = CareTheme.typography.subtitle1,
                color = CareTheme.colors.gray900,
                modifier = Modifier.padding(bottom = 20.dp),
            )

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
                        onClick = { onGenderChanged(Gender.WOMAN) },
                        enable = gender == Gender.WOMAN,
                        modifier = Modifier.width(104.dp),
                    )

                    CareChipBasic(
                        text = Gender.MAN.displayName,
                        onClick = { onGenderChanged(Gender.MAN) },
                        enable = gender == Gender.MAN,
                        modifier = Modifier.width(104.dp),
                    )
                }
            }

            LabeledContent(
                subtitle = "출생연도",
                modifier = Modifier.fillMaxWidth(),
            ) {
                CareTextField(
                    value = birthYear,
                    onValueChanged = onBirthYearChanged,
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
                    value = weight,
                    onValueChanged = onWeightChanged,
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
                            enable = careLevel == level.toString(),
                            onClick = { onCareLevelChanged(level.toString()) },
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
                            enable = mentalStatus == status,
                            onClick = { onMentalStatusChanged(status) },
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
                    value = disease,
                    onValueChanged = onDiseaseChanged,
                    hint = "고객의 고객이 현재 앓고 있는 질병 또는 병력을 입력해주세요.",
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
                        onClick = { onMealAssistanceChanged(true) },
                        enable = isMealAssistance == true,
                        modifier = Modifier.width(104.dp),
                    )

                    CareChipBasic(
                        text = stringResource(R.string.unnecessary),
                        onClick = { onMealAssistanceChanged(false) },
                        enable = isMealAssistance == false,
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
                        onClick = { onBowelAssistanceChanged(true) },
                        enable = isBowelAssistance == true,
                        modifier = Modifier.width(104.dp),
                    )

                    CareChipBasic(
                        text = stringResource(R.string.unnecessary),
                        onClick = { onBowelAssistanceChanged(false) },
                        enable = isBowelAssistance == false,
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
                        onClick = { onWalkingAssistanceChanged(true) },
                        enable = isWalkingAssistance == true,
                        modifier = Modifier.width(104.dp),
                    )

                    CareChipBasic(
                        text = stringResource(R.string.unnecessary),
                        onClick = { onWalkingAssistanceChanged(false) },
                        enable = isWalkingAssistance == false,
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
                            onClick = { onLifeAssistanceChanged(assistance) },
                            enable = assistance in lifeAssistance,
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
                    value = speciality,
                    hint = "추가적으로 요구사항이 있다면 작성해주세요.(예: 어쩌고저쩌고)",
                    onValueChanged = onSpecialityChanged,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 28.dp)
        ) {
            Text(
                text = "추가 지원 정보",
                style = CareTheme.typography.subtitle1,
                color = CareTheme.colors.gray900,
                modifier = Modifier.padding(bottom = 20.dp),
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
                        onClick = { onExperiencePreferredChanged(false) },
                        enable = isExperiencePreferred == false,
                        modifier = Modifier.width(104.dp),
                    )

                    CareChipBasic(
                        text = "경력 우대",
                        onClick = { onExperiencePreferredChanged(true) },
                        enable = isExperiencePreferred == true,
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
                            onClick = { onApplyMethodChanged(method) },
                            enable = method in applyMethod,
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
                        ApplyDeadlineChipState.entries.forEach { state ->
                            CareChipBasic(
                                text = state.displayName,
                                onClick = { onApplyDeadlineChipStateChanged(state) },
                                enable = applyDeadlineChipState == state,
                                modifier = Modifier.width(104.dp),
                            )
                        }
                    }

                    if (applyDeadlineChipState == ApplyDeadlineChipState.SET_DEADLINE) {
                        CareTextField(
                            value = applyDeadline,
                            onValueChanged = onApplyDeadlineChanged,
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