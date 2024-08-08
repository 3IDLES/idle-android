package com.idle.signup.worker.step

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareChipBasic
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.Gender
import com.idle.signin.worker.WorkerSignUpStep
import com.idle.signin.worker.WorkerSignUpStep.INFO

@Composable
internal fun WorkerInformationScreen(
    workerName: String,
    birthYear: String,
    gender: Gender,
    onWorkerNameChanged: (String) -> Unit,
    onBirthYearChanged: (String) -> Unit,
    onGenderChanged: (Gender) -> Unit,
    setSignUpStep: (WorkerSignUpStep) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler { setSignUpStep(WorkerSignUpStep.findStep(INFO.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.worker_name_title),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.name),
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = birthYear,
                hint = stringResource(id = R.string.worker_birth_year_hint),
                onValueChanged = onBirthYearChanged,
                keyboardType = KeyboardType.Number,
                onDone = {},
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
                    onClick = {
                        onGenderChanged(Gender.WOMAN)
                        if (workerName.isNotBlank() && gender != Gender.NONE && birthYear.isNotBlank()) {
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
                        if (workerName.isNotBlank() && gender != Gender.NONE && birthYear.isNotBlank()) {
                            setSignUpStep(WorkerSignUpStep.findStep(INFO.step + 1))
                        }
                    },
                    enable = gender == Gender.MAN,
                    modifier = Modifier.width(104.dp),
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.next),
            enable = workerName.isNotBlank() && gender != Gender.NONE && birthYear.isNotBlank(),
            onClick = {
                setSignUpStep(WorkerSignUpStep.findStep(INFO.step + 1))
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}