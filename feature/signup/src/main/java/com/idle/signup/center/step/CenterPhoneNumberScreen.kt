package com.idle.signup.center.step

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
import com.idle.signin.center.CenterSignUpStep

@Composable
internal fun CenterPhoneNumberScreen(
    centerPhoneNumber: String,
    centerAuthCodeTimerMinute: String,
    centerAuthCodeTimerSeconds: String,
    centerAuthCode: String,
    isConfirmAuthCode: Boolean,
    businessRegistrationProcessed: Boolean,
    onCenterPhoneNumberChanged: (String) -> Unit,
    onCenterAuthCodeChanged: (String) -> Unit,
    setSignUpStep: (CenterSignUpStep) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
    setBusinessRegistrationProcessed: (Boolean) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(isConfirmAuthCode) {
        if (isConfirmAuthCode && !businessRegistrationProcessed) {
            setSignUpStep(CenterSignUpStep.BUSINESS_REGISTRATION_NUMBER)
            setBusinessRegistrationProcessed(true)
        }
    }

    BackHandler { setSignUpStep(CenterSignUpStep.NAME) }

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
                modifier = Modifier.fillMaxWidth(),
            ) {
                CareTextField(
                    value = centerPhoneNumber,
                    hint = "전화번호를 입력해주세요.",
                    onValueChanged = onCenterPhoneNumberChanged,
                    readOnly = (centerAuthCodeTimerMinute != "" && centerAuthCodeTimerSeconds != ""),
                    onDone = { if (centerPhoneNumber.length == 11) sendPhoneNumber() },
                    modifier = Modifier.weight(1f)
                        .focusRequester(focusRequester),
                )

                CareButtonSmall(
                    enable = centerPhoneNumber.length == 11 &&
                            !(centerAuthCodeTimerMinute != "" && centerAuthCodeTimerSeconds != ""),
                    text = "인증",
                    onClick = sendPhoneNumber,
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
        ) {
            Text(
                text = "인증번호",
                style = CareTheme.typography.subtitle4,
                color = CareTheme.colors.gray500,
            )

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                CareTextField(
                    value = centerAuthCode,
                    hint = "",
                    onValueChanged = onCenterAuthCodeChanged,
                    supportingText = if (isConfirmAuthCode) "인증이 완료되었습니다." else "",
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
                    modifier = Modifier.weight(1f),
                )

                CareButtonSmall(
                    enable = centerAuthCode.isNotBlank() && !isConfirmAuthCode,
                    text = "확인",
                    onClick = confirmAuthCode,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            enable = isConfirmAuthCode,
            onClick = { setSignUpStep(CenterSignUpStep.BUSINESS_REGISTRATION_NUMBER) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}