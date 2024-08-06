package com.idle.signin.center.newpassword.step

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareButtonSmall
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.center.newpassword.NewPasswordStep
import com.idle.signin.center.newpassword.NewPasswordStep.GENERATE_NEW_PASSWORD

@Composable
internal fun PhoneNumberScreen(
    phoneNumber: String,
    certificationNumber: String,
    onPhoneNumberChanged: (String) -> Unit,
    onAuthCodeChanged: (String) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
    setNewPasswordProcess: (NewPasswordStep) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareTextField(
                    value = phoneNumber,
                    hint = stringResource(id = R.string.phone_number_hint),
                    onValueChanged = onPhoneNumberChanged,
                    onDone = { sendPhoneNumber() },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                )

                CareButtonSmall(
                    enable = phoneNumber.length == 13,
                    text = stringResource(id = R.string.verification),
                    onClick = sendPhoneNumber,
                )
            }
        }

        LabeledContent(
            subtitle = stringResource(id = R.string.confirm_code),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareTextField(
                    value = certificationNumber,
                    hint = "",
                    onValueChanged = onAuthCodeChanged,
                    onDone = { confirmAuthCode() },
                    modifier = Modifier.weight(1f),
                )

                CareButtonSmall(
                    enable = certificationNumber.isNotBlank(),
                    text = stringResource(id = R.string.confirm),
                    onClick = confirmAuthCode,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.next),
            enable = certificationNumber.isNotBlank(),
            onClick = { setNewPasswordProcess(GENERATE_NEW_PASSWORD) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}