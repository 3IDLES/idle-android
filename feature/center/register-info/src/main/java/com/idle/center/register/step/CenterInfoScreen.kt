package com.idle.center.register.step

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.idle.center.register.LogRegistrationStep
import com.idle.center.register.RegistrationStep
import com.idle.center.register.RegistrationStep.ADDRESS
import com.idle.center.register.RegistrationStep.INFO
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
internal fun CenterInfoScreen(
    centerName: String,
    centerNumber: String,
    onCenterNameChanged: (String) -> Unit,
    onCenterNumberChanged: (String) -> Unit,
    setRegistrationStep: (RegistrationStep) -> Unit,
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
            text = stringResource(id = R.string.center_info_hint),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.name),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = centerName,
                hint = stringResource(id = R.string.center_name_hint),
                onValueChanged = onCenterNameChanged,
                onDone = {
                    if (centerName.isNotBlank()) {
                        focusManager.moveFocus(FocusDirection.Down)
                        keyboardController?.show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )
        }

        LabeledContent(
            subtitle = stringResource(id = R.string.center_number),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = centerNumber,
                hint = stringResource(id = R.string.center_number_hint),
                onValueChanged = onCenterNumberChanged,
                keyboardType = KeyboardType.Number,
                onDone = {
                    if (centerName.isNotBlank() && centerNumber.isNotBlank())
                        setRegistrationStep(RegistrationStep.findStep(INFO.step + 1))
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.next),
            enable = centerName.isNotBlank() && centerNumber.isNotBlank(),
            onClick = { setRegistrationStep(RegistrationStep.findStep(INFO.step + 1)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }

    LogRegistrationStep(step = INFO)
}