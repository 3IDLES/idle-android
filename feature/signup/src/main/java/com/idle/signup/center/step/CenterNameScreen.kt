package com.idle.signup.center.step

import androidx.compose.foundation.layout.Column
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
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signup.LogCenterSignUpStep
import com.idle.signup.center.CenterSignUpStep
import com.idle.signup.center.CenterSignUpStep.NAME

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
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.enter_your_name),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.black,
            modifier = Modifier.padding(bottom = 28.dp),
            )

        CareTextField(
            value = centerName,
            hint = stringResource(id = R.string.enter_your_name_hint),
            onValueChanged = onCenterNameChanged,
            onDone = {
                if (centerName.isNotBlank()) {
                    setSignUpStep(CenterSignUpStep.PHONE_NUMBER)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.next),
            enable = centerName.isNotBlank(),
            onClick = { setSignUpStep(CenterSignUpStep.findStep(NAME.step + 1)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }

    LogCenterSignUpStep(NAME)
}