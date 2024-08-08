package com.idle.signup.worker.step

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.worker.WorkerSignUpStep
import com.idle.signin.worker.WorkerSignUpStep.NAME
import com.idle.designresource.R

@Composable
internal fun WorkerNameScreen(
    workerName: String,
    onWorkerNameChanged: (String) -> Unit,
    setSignUpStep: (WorkerSignUpStep) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.worker_name_hint),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        CareTextField(
            value = workerName,
            hint = stringResource(id = R.string.worker_name_hint),
            onValueChanged = onWorkerNameChanged,
            onDone = {
                if (workerName.isNotBlank()) {
                    setSignUpStep(WorkerSignUpStep.findStep(NAME.step + 1))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.next),
            enable = workerName.isNotBlank(),
            onClick = {
                if (workerName.isNotBlank()) {
                    setSignUpStep(WorkerSignUpStep.findStep(NAME.step + 1))
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}