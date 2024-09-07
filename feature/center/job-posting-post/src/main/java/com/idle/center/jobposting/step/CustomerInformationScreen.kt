package com.idle.center.jobposting.step

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.idle.center.jobposting.JobPostingStep
import com.idle.center.jobposting.JobPostingStep.CUSTOMER_INFORMATION
import com.idle.center.jobposting.LogJobPostingStep
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareChipBasic
import com.idle.designsystem.compose.component.CareChipShort
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.designsystem.compose.foundation.PretendardMedium
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.MentalStatus

@Composable
internal fun CustomerInformationScreen(
    clientName: String,
    gender: Gender,
    birthYear: String,
    weight: String,
    careLevel: String,
    mentalStatus: MentalStatus,
    disease: String,
    onClientNameChanged: (String) -> Unit,
    onGenderChanged: (Gender) -> Unit,
    onBirthYearChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit,
    onCareLevelChanged: (String) -> Unit,
    onMentalStatusChanged: (MentalStatus) -> Unit,
    onDiseaseChanged: (String) -> Unit,
    setJobPostingStep: (JobPostingStep) -> Unit,
    showSnackBar: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    BackHandler { setJobPostingStep(JobPostingStep.findStep(CUSTOMER_INFORMATION.step - 1)) }

    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        Text(
            text = stringResource(id = R.string.customer_info_title),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.name),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = clientName,
                onValueChanged = onClientNameChanged,
                hint = stringResource(id = R.string.customer_name_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
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
            subtitle = stringResource(id = R.string.birth_year),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = birthYear,
                onValueChanged = {
                    onBirthYearChanged(it)
                    if (it.length == 4) {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                },
                keyboardType = KeyboardType.Number,
                hint = stringResource(id = R.string.birth_year_hint),
                onDone = {
                    if (birthYear.isNotBlank()) focusManager.moveFocus(FocusDirection.Down)
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        LabeledContent(
            subtitle = stringResource(id = R.string.weight),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = weight,
                onValueChanged = {
                    onWeightChanged(it)
                    if (it.length == 3) {
                        keyboardController?.hide()
                    }
                },
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
                        enable = careLevel == level.toString(),
                        onClick = { onCareLevelChanged(level.toString()) },
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
                hint = stringResource(id = R.string.disease_hint),
                onDone = { setJobPostingStep(JobPostingStep.findStep(CUSTOMER_INFORMATION.step + 1)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.next),
            enable = gender != Gender.NONE && birthYear.isNotBlank() && weight.isNotBlank() && mentalStatus != MentalStatus.UNKNOWN,
            onClick = {
                if ((birthYear.toIntOrNull() ?: return@CareButtonLarge) < 1900) {
                    showSnackBar("출생년도가 잘못되었습니다.|Error")
                    return@CareButtonLarge
                }

                setJobPostingStep(JobPostingStep.findStep(CUSTOMER_INFORMATION.step + 1))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }

    LogJobPostingStep(step = CUSTOMER_INFORMATION)
}