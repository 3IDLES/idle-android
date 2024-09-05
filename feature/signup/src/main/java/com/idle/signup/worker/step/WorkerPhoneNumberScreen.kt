package com.idle.signup.worker.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareButtonSmall
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.worker.WorkerSignUpStep
import com.idle.signin.worker.WorkerSignUpStep.INFO
import com.idle.signin.worker.WorkerSignUpStep.PHONE_NUMBER
import com.idle.signup.LogWorkerSignUpStep

@Composable
internal fun WorkerPhoneNumberScreen(
    workerPhoneNumber: String,
    workerAuthCodeTimerMinute: String,
    workerAuthCodeTimerSeconds: String,
    workerAuthCode: String,
    isConfirmAuthCode: Boolean,
    addressProcessed: Boolean,
    onWorkerPhoneNumberChanged: (String) -> Unit,
    onWorkerAuthCodeChanged: (String) -> Unit,
    setSignUpStep: (WorkerSignUpStep) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
    setAddressProcessed: (Boolean) -> Unit,
    navigateToAuth: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(isConfirmAuthCode) {
        if (isConfirmAuthCode && !addressProcessed) {
            setSignUpStep(WorkerSignUpStep.findStep(PHONE_NUMBER.step + 1))
            setAddressProcessed(true)
        }
    }

    BackHandler { navigateToAuth() }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.phone_number_hint),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.phone_number),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareTextField(
                    value = workerPhoneNumber,
                    hint = stringResource(id = R.string.phone_number_hint),
                    onValueChanged = onWorkerPhoneNumberChanged,
                    readOnly = (workerAuthCodeTimerMinute != "" && workerAuthCodeTimerSeconds != ""),
                    onDone = { if (workerPhoneNumber.length == 11) sendPhoneNumber() },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                )

                CareButtonSmall(
                    enable = workerPhoneNumber.length == 11 &&
                            !(workerAuthCodeTimerMinute != "" && workerAuthCodeTimerSeconds != ""),
                    text = stringResource(id = R.string.verification),
                    onClick = sendPhoneNumber,
                )
            }
        }

        if (workerAuthCodeTimerMinute.isNotBlank()) {
            LabeledContent(
                subtitle = stringResource(id = R.string.confirm_code),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CareTextField(
                        value = workerAuthCode,
                        hint = "",
                        onValueChanged = onWorkerAuthCodeChanged,
                        onDone = { confirmAuthCode() },
                        supportingText = if (isConfirmAuthCode) "인증이 완료되었습니다." else "",
                        readOnly = !(workerAuthCodeTimerMinute != "" && workerAuthCodeTimerSeconds != "") || isConfirmAuthCode,
                        leftComponent = {
                            if (workerAuthCodeTimerMinute != "" && workerAuthCodeTimerSeconds != "") {
                                Text(
                                    text = "$workerAuthCodeTimerMinute:$workerAuthCodeTimerSeconds",
                                    style = CareTheme.typography.body3,
                                    color = if (!isConfirmAuthCode) CareTheme.colors.gray500 else CareTheme.colors.gray200,
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )

                    CareButtonSmall(
                        enable = workerAuthCode.isNotBlank() && !isConfirmAuthCode,
                        text = stringResource(id = R.string.confirm),
                        onClick = confirmAuthCode,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.next),
            enable = isConfirmAuthCode,
            onClick = { setSignUpStep(WorkerSignUpStep.findStep(PHONE_NUMBER.step + 1)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }

    LogWorkerSignUpStep(PHONE_NUMBER)
}