package com.idle.withdrawal.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonMedium
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.withdrawal.WithdrawalStep

@Composable
internal fun PasswordScreen(
    password: String,
    onPasswordChanged: (String) -> Unit,
    setWithdrawalStep: (WithdrawalStep) -> Unit,
    withdrawal: () -> Unit,
    navigateToSetting: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler { setWithdrawalStep(WithdrawalStep.findStep(WithdrawalStep.PASSWORD.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = stringResource(id = R.string.withdrawal_password_verification_title),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.black,
            modifier = Modifier.padding(bottom = 36.dp),
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.password),
            modifier = Modifier.fillMaxWidth()
        ) {
            CareTextField(
                value = password,
                hint = stringResource(id = R.string.password_hint),
                onValueChanged = onPasswordChanged,
                visualTransformation = PasswordVisualTransformation(),
                onDone = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        ) {
            CareButtonMedium(
                text = stringResource(id = R.string.cancel_short),
                textColor = CareTheme.colors.gray300,
                containerColor = CareTheme.colors.white000,
                border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray200),
                onClick = { navigateToSetting() },
                modifier = Modifier.weight(1f),
            )

            CareButtonMedium(
                text = stringResource(id = R.string.withdrawal),
                textColor = CareTheme.colors.white000,
                containerColor = CareTheme.colors.red,
                enable = password.isNotBlank(),
                onClick = withdrawal,
                modifier = Modifier.weight(1f),
            )
        }
    }
}