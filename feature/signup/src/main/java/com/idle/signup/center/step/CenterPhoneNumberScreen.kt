package com.idle.signup.center.step

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
import com.idle.signin.center.CenterSignUpStep
import com.idle.signin.center.CenterSignUpStep.PHONE_NUMBER
import com.idle.signup.LogCenterSignUpStep

@Composable
internal fun CenterPhoneNumberScreen(
    centerPhoneNumber: String,
    centerAuthCodeTimerMinute: String,
    centerAuthCodeTimerSeconds: String,
    centerAuthCode: String,
    isConfirmAuthCode: Boolean,
    onCenterPhoneNumberChanged: (String) -> Unit,
    onCenterAuthCodeChanged: (String) -> Unit,
    setSignUpStep: (CenterSignUpStep) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler { setSignUpStep(CenterSignUpStep.findStep(PHONE_NUMBER.step - 1)) }

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
                modifier = Modifier.fillMaxWidth(),
            ) {
                CareTextField(
                    value = centerPhoneNumber,
                    hint = stringResource(id = R.string.phone_number_hint),
                    onValueChanged = {
                        onCenterPhoneNumberChanged(it)
                        if (it.length == 11) {
                            sendPhoneNumber()
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    },
                    readOnly = (centerAuthCodeTimerMinute != "" && centerAuthCodeTimerSeconds != ""),
                    onDone = {
                        if (centerPhoneNumber.length == 11) sendPhoneNumber()
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                )

                CareButtonSmall(
                    enable = centerPhoneNumber.length == 11 &&
                            !(centerAuthCodeTimerMinute != "" && centerAuthCodeTimerSeconds != ""),
                    text = stringResource(id = R.string.verification),
                    onClick = sendPhoneNumber,
                )
            }
        }

        if (centerAuthCodeTimerMinute.isNotBlank()) {
            LabeledContent(
                subtitle = stringResource(id = R.string.confirm_code),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        CareTextField(
                            value = centerAuthCode,
                            hint = "",
                            onValueChanged = onCenterAuthCodeChanged,
                            readOnly = !(centerAuthCodeTimerMinute != "" && centerAuthCodeTimerSeconds != "") || isConfirmAuthCode,
                            onDone = { if (centerAuthCode.isNotBlank()) confirmAuthCode() },
                            leftComponent = {
                                if (centerAuthCodeTimerMinute != "" && centerAuthCodeTimerSeconds != "") {
                                    Text(
                                        text = "$centerAuthCodeTimerMinute:$centerAuthCodeTimerSeconds",
                                        style = CareTheme.typography.body3,
                                        color = if (!isConfirmAuthCode) CareTheme.colors.gray500 else CareTheme.colors.gray200,
                                    )
                                }
                            },
                        )

                        Text(
                            text = if (isConfirmAuthCode) "인증이 완료되었습니다." else "",
                            style = CareTheme.typography.caption,
                            color = CareTheme.colors.gray300,
                        )
                    }

                    CareButtonSmall(
                        enable = centerAuthCode.isNotBlank() && !isConfirmAuthCode,
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
            onClick = { setSignUpStep(CenterSignUpStep.findStep(PHONE_NUMBER.step + 1)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }

    LogCenterSignUpStep(PHONE_NUMBER)
}