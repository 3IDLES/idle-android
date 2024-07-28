package com.idle.signup.center.process

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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.idle.center.register.VerificationProcess
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
internal fun CenterInfoScreen(
    centerName: String,
    centerNumber: String,
    onCenterNameChanged: (String) -> Unit,
    onCenterNumberChanged: (String) -> Unit,
    setVerificationProcess: (VerificationProcess) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "센터 정보를 입력해주세요",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = "이름",
                style = CareTheme.typography.subtitle4,
                color = CareTheme.colors.gray500,
            )

            CareTextField(
                value = centerName,
                hint = "센터 이름을 입력해주세요.",
                onValueChanged = onCenterNameChanged,
                onDone = { if (centerName.isNotBlank()){
                    focusManager.moveFocus(FocusDirection.Down)
                    keyboardController?.show()
                } },
                modifier = Modifier.fillMaxWidth()
                    .focusRequester(focusRequester),
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = "센터 연락처",
                style = CareTheme.typography.subtitle4,
                color = CareTheme.colors.gray500,
            )

            CareTextField(
                value = centerNumber,
                hint = "지원자들의 연락을 받을 번호를 입력해주세요.",
                onValueChanged = onCenterNumberChanged,
                keyboardType = KeyboardType.Number,
                onDone = {
                    if (centerName.isNotBlank() && centerNumber.isNotBlank()) setVerificationProcess(
                        VerificationProcess.ADDRESS
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            enable = centerName.isNotBlank() && centerNumber.isNotBlank(),
            onClick = { setVerificationProcess(VerificationProcess.ADDRESS) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}