package com.idle.signin.center.newpassword.step

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.ConditionRow
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.center.newpassword.NewPasswordStep
import com.idle.signin.center.newpassword.NewPasswordStep.PHONE_NUMBER

@Composable
internal fun GenerateNewPasswordScreen(
    newPassword: String,
    newPasswordForConfirm: String,
    isPasswordLengthValid: Boolean,
    isPasswordContainsLetterAndDigit: Boolean,
    isPasswordNoWhitespace: Boolean,
    isPasswordNoSequentialChars: Boolean,
    isPasswordValid: Boolean,
    onNewPasswordChanged: (String) -> Unit,
    onNewPasswordForConfirmChanged: (String) -> Unit,
    setNewPasswordProcess: (NewPasswordStep) -> Unit,
    generateNewPassword: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler { setNewPasswordProcess(PHONE_NUMBER) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = stringResource(id = R.string.new_password_title),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.black,
            modifier = Modifier.padding(bottom = 28.dp),
        )

        LabeledContent(
            subtitle = stringResource(R.string.set_password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        ) {
            CareTextField(
                value = newPassword,
                hint = stringResource(id = R.string.password_hint),
                onValueChanged = onNewPasswordChanged,
                visualTransformation = PasswordVisualTransformation(),
                onDone = { focusManager.moveFocus(FocusDirection.Down) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )
        }

        Text(
            text = stringResource(id = R.string.password_description),
            style = CareTheme.typography.body3,
            color = CareTheme.colors.gray500,
            modifier = Modifier.padding(bottom = 6.dp),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        ) {
            ConditionRow(
                isValid = isPasswordLengthValid,
                conditionText = stringResource(R.string.condition_password_length),
            )

            ConditionRow(
                isValid = isPasswordContainsLetterAndDigit,
                conditionText = stringResource(R.string.condition_letter_and_digit),
            )

            ConditionRow(
                isValid = isPasswordNoWhitespace,
                conditionText = stringResource(R.string.condition_no_whitespace),
            )

            ConditionRow(
                isValid = isPasswordNoSequentialChars,
                conditionText = stringResource(R.string.condition_no_sequential_chars),
            )
        }

        LabeledContent(
            subtitle = stringResource(id = R.string.confirm_password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
        ) {
            CareTextField(
                value = newPasswordForConfirm,
                hint = stringResource(id = R.string.confirm_password_hint),
                onValueChanged = onNewPasswordForConfirmChanged,
                visualTransformation = PasswordVisualTransformation(),
                isError = newPasswordForConfirm.isNotBlank() && newPassword != newPasswordForConfirm,
                onDone = { if (isPasswordValid) generateNewPassword() },
                modifier = Modifier.fillMaxWidth()
            )
        }


        Text(
            text = if (newPasswordForConfirm.isNotBlank() && newPassword != newPasswordForConfirm)
                stringResource(R.string.login_error_description) else "",
            style = CareTheme.typography.caption1,
            color = CareTheme.colors.red,
        )

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.change_password),
            enable = isPasswordValid,
            onClick = generateNewPassword,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }
}