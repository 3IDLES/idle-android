package com.idle.signup.center.process

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.center.CenterSignUpProcess

@Composable
internal fun CenterPhoneNumberScreen(
    centerPhoneNumber: String,
    centerCertificationNumber: String,
    onCenterPhoneNumberChanged: (String) -> Unit,
    onCenterAuthCodeChanged: (String) -> Unit,
    setSignUpProcess: (CenterSignUpProcess) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler { setSignUpProcess(CenterSignUpProcess.NAME) }

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

            Row(modifier = Modifier.fillMaxWidth()) {
                CareTextField(
                    value = centerPhoneNumber,
                    hint = "전화번호를 입력해주세요.",
                    onValueChanged = onCenterPhoneNumberChanged,
                    onDone = { },
                    modifier = Modifier.fillMaxWidth()
                        .focusRequester(focusRequester)
                )

                Button(onClick = sendPhoneNumber) {
                    Text(text = "인증")
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(text = "인증번호")

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = centerCertificationNumber,
                    onValueChange = onCenterAuthCodeChanged
                )

                Button(onClick = confirmAuthCode) {
                    Text(text = "확인")
                }
            }
        }

        Button(onClick = { setSignUpProcess(CenterSignUpProcess.BUSINESS_REGISTRAION_NUMBER) }) {
            Text(text = "다음")
        }
    }
}