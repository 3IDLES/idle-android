package com.idle.signup.worker.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonMedium
import com.idle.designsystem.compose.component.CareChipBasic
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.Gender
import com.idle.signin.worker.WorkerSignUpStep
import com.idle.signin.worker.WorkerSignUpStep.INFO
import com.idle.signup.LogWorkerSignUpStep

@Composable
internal fun WorkerInformationScreen(
    workerName: String,
    birthYear: String,
    gender: Gender,
    onWorkerNameChanged: (String) -> Unit,
    onBirthYearChanged: (String) -> Unit,
    onGenderChanged: (Gender) -> Unit,
    setSignUpStep: (WorkerSignUpStep) -> Unit,
    showSnackBar: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler { setSignUpStep(WorkerSignUpStep.findStep(INFO.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.worker_name_title),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.black,
            modifier = Modifier.padding(bottom = 28.dp),
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.name),
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 32.dp),
            ) {
            CareTextField(
                value = workerName,
                hint = stringResource(id = R.string.worker_name_hint),
                onValueChanged = onWorkerNameChanged,
                onDone = { if (workerName.isNotBlank()) focusManager.moveFocus(FocusDirection.Down) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
        }

        LabeledContent(
            subtitle = stringResource(id = R.string.birth_year),
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 32.dp),
            ) {
            CareTextField(
                value = birthYear,
                hint = stringResource(id = R.string.worker_birth_year_hint),
                onValueChanged = {
                    onBirthYearChanged(it)
                    if (it.length == 4) {
                        keyboardController?.hide()
                    }
                },
                keyboardType = KeyboardType.Number,
                onDone = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }

        LabeledContent(
            subtitle = stringResource(id = R.string.gender),
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 32.dp),
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

                        if ((birthYear.toIntOrNull() ?: return@CareChipBasic) < 1900) {
                            return@CareChipBasic
                        }

                        if (workerName.isNotBlank() && birthYear.length == 4) {
                            setSignUpStep(WorkerSignUpStep.findStep(INFO.step + 1))
                        }
                    },
                    enable = gender == Gender.WOMAN,
                    modifier = Modifier.width(104.dp),
                )

                CareChipBasic(
                    text = Gender.MAN.displayName,
                    onClick = {
                        onGenderChanged(Gender.MAN)

                        if ((birthYear.toIntOrNull() ?: return@CareChipBasic) < 1900) {
                            return@CareChipBasic
                        }

                        if (workerName.isNotBlank() && birthYear.length == 4) {
                            setSignUpStep(WorkerSignUpStep.findStep(INFO.step + 1))
                        }
                    },
                    enable = gender == Gender.MAN,
                    modifier = Modifier.width(104.dp),
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 28.dp),
        ) {
            CareButtonMedium(
                text = stringResource(id = R.string.previous),
                textColor = CareTheme.colors.gray300,
                containerColor = CareTheme.colors.white000,
                border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray200),
                onClick = { setSignUpStep(WorkerSignUpStep.findStep(INFO.step - 1)) },
                modifier = Modifier.weight(1f),
            )

            CareButtonMedium(
                text = stringResource(id = R.string.next),
                enable = workerName.isNotBlank() && gender != Gender.NONE && birthYear.length == 4,
                onClick = {
                    if ((birthYear.toIntOrNull() ?: return@CareButtonMedium) < 1900) {
                        showSnackBar("출생년도가 잘못되었습니다.|Error")
                        return@CareButtonMedium
                    }

                    setSignUpStep(WorkerSignUpStep.findStep(INFO.step + 1))
                },
                modifier = Modifier.weight(1f),
            )
        }
    }

    LogWorkerSignUpStep(INFO)
}