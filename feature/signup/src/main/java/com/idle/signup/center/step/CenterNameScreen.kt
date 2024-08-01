package com.idle.signup.center.step

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
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.center.CenterSignUpStep

@Composable
internal fun CenterNameScreen(
    centerName: String,
    onCenterNameChanged: (String) -> Unit,
    setSignUpStep: (CenterSignUpStep) -> Unit,
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
            text = "성함을 입력해주세요",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        CareTextField(
            value = centerName,
            hint = "성함을 입력해주세요.",
            onValueChanged = onCenterNameChanged,
            onDone = {
                if (centerName.isNotBlank()) {
                    setSignUpStep(CenterSignUpStep.PHONE_NUMBER)
                }
            },
            modifier = Modifier.fillMaxWidth()
                .focusRequester(focusRequester)
        )

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            enable = centerName.isNotBlank(),
            onClick = { setSignUpStep(CenterSignUpStep.PHONE_NUMBER) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}