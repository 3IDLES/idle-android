package com.idle.signup.center.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.idle.center.jobposting.JobPostingStep
import com.idle.center.jobposting.JobPostingStep.ADDRESS
import com.idle.center.jobposting.JobPostingStep.CUSTOMER_INFORMATION
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareChipBasic
import com.idle.designsystem.compose.component.CareChipShort
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.designsystem.compose.foundation.PretendardMedium
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.MentalStatus

@Composable
internal fun CustomerInformationScreen(
    gender: Gender,
    birthYear: String,
    weight: String,
    careLevel: String,
    mentalStatus: MentalStatus,
    disease: String,
    onGenderChanged: (Gender) -> Unit,
    onBirthYearChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit,
    onCareLevelChanged: (String) -> Unit,
    onMentalStatusChanged: (MentalStatus) -> Unit,
    onDiseaseChanged: (String) -> Unit,
    setJobPostingStep: (JobPostingStep) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    BackHandler { setJobPostingStep(JobPostingStep.findStep(ADDRESS.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier.fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 30.dp),
    ) {
        Text(
            text = "고객 정보를 입력해주세요.",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
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
                    onClick = {
                        onGenderChanged(Gender.WOMAN)
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                    enable = gender == Gender.WOMAN,
                    modifier = Modifier.width(104.dp),
                )

                CareChipBasic(
                    text = Gender.MAN.displayName,
                    onClick = {
                        onGenderChanged(Gender.MAN)
                        focusManager.moveFocus(FocusDirection.Down)
                    },
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
                onDone = {
                    if (birthYear.isNotBlank()) focusManager.moveFocus(FocusDirection.Down)
                },
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
                hint = "고객이 현재 앓고 있는 질병 또는 병력을 입력해주세요.",
                onDone = { setJobPostingStep(JobPostingStep.findStep(CUSTOMER_INFORMATION.step + 1)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            enable = gender != Gender.NONE && birthYear.isNotBlank() && weight.isNotBlank() && mentalStatus != MentalStatus.UNKNOWN,
            onClick = { setJobPostingStep(JobPostingStep.findStep(CUSTOMER_INFORMATION.step + 1)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}