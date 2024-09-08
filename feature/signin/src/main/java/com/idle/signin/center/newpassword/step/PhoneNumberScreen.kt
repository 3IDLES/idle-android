package com.idle.signin.center.newpassword.step

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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareButtonSmall
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.center.newpassword.NewPasswordStep

@Composable
internal fun PhoneNumberScreen(
    phoneNumber: String,
    authCode: String,
    timerMinute: String,
    timerSeconds: String,
    isConfirmAuthCode: Boolean,
    onPhoneNumberChanged: (String) -> Unit,
    onAuthCodeChanged: (String) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
    setNewPasswordProcess: (NewPasswordStep) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
                    value = phoneNumber,
                    hint = stringResource(id = R.string.phone_number_hint),
                    onValueChanged = {
                        onPhoneNumberChanged(it)
                        if (it.length == 11) {
                            sendPhoneNumber()
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    },
                    readOnly = (timerMinute != "" && timerSeconds != ""),
                    onDone = { if (phoneNumber.length == 11) sendPhoneNumber() },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                )

                CareButtonSmall(
                    enable = phoneNumber.length == 11 &&
                            !(timerMinute != "" && timerSeconds != ""),
                    text = stringResource(id = R.string.verification),
                    onClick = sendPhoneNumber,
                )
            }
        }

        if (timerMinute.isNotBlank()) {
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
                        value = authCode,
                        hint = "",
                        onValueChanged = onAuthCodeChanged,
                        onDone = { confirmAuthCode() },
                        supportingText = if (isConfirmAuthCode) "인증이 완료되었습니다." else "",
                        readOnly = !(timerMinute != "" && timerSeconds != "") || isConfirmAuthCode,
                        leftComponent = {
                            if (timerMinute != "" && timerSeconds != "") {
                                Text(
                                    text = "$timerMinute:$timerSeconds",
                                    style = CareTheme.typography.body3,
                                    color = if (!isConfirmAuthCode) CareTheme.colors.gray500 else CareTheme.colors.gray200,
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )

                    CareButtonSmall(
                        enable = authCode.isNotBlank() && !isConfirmAuthCode,
                        text = stringResource(id = R.string.confirm_short),
                        onClick = confirmAuthCode,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.next),
            enable = isConfirmAuthCode,
            onClick = { setNewPasswordProcess(NewPasswordStep.GENERATE_NEW_PASSWORD) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }
}