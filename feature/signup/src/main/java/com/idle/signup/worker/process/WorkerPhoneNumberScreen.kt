package com.idle.signup.worker.process

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareButtonSmall
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.worker.WorkerSignUpProcess

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
    setSignUpProcess: (WorkerSignUpProcess) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
    setAddressProcessed: (Boolean) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(isConfirmAuthCode) {
        if (isConfirmAuthCode && !addressProcessed) {
            setSignUpProcess(WorkerSignUpProcess.ADDRESS)
            setAddressProcessed(true)
        }
    }

    BackHandler { setSignUpProcess(WorkerSignUpProcess.GENDER) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "전화번호를 입력해주세요",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = "전화번호",
                style = CareTheme.typography.subtitle4,
                color = CareTheme.colors.gray500,
            )

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareTextField(
                    value = workerPhoneNumber,
                    hint = "전화번호를 입력해주세요.",
                    onValueChanged = onWorkerPhoneNumberChanged,
                    readOnly = (workerAuthCodeTimerMinute != "" && workerAuthCodeTimerSeconds != ""),
                    onDone = { if (workerPhoneNumber.length == 11) sendPhoneNumber() },
                    modifier = Modifier.weight(1f)
                        .focusRequester(focusRequester),
                )

                CareButtonSmall(
                    enable = workerPhoneNumber.length == 11 &&
                            !(workerAuthCodeTimerMinute != "" && workerAuthCodeTimerSeconds != ""),
                    text = "인증",
                    onClick = sendPhoneNumber,
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = "인증번호",
                style = CareTheme.typography.subtitle4,
                color = CareTheme.colors.gray500,
            )

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
                    enable = workerAuthCode.isNotBlank(),
                    text = "확인",
                    onClick = confirmAuthCode,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            enable = isConfirmAuthCode,
            onClick = { setSignUpProcess(WorkerSignUpProcess.ADDRESS) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}