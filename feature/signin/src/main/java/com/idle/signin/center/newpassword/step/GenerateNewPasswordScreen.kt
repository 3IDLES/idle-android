package com.idle.signin.center.newpassword.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareTextField
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val imageResource =
                    if (isPasswordContainsLetterAndDigit) R.drawable.ic_right_condition
                    else R.drawable.ic_not_right_condition

                Image(
                    painter = painterResource(imageResource),
                    contentDescription = "",
                )

                val textColor = if (isPasswordContainsLetterAndDigit) CareTheme.colors.green
                else CareTheme.colors.red

                Text(
                    text = "영문자와 숫자 반드시 하나씩 포함",
                    style = CareTheme.typography.body3,
                    color = textColor,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val imageResource = if (isPasswordNoWhitespace) R.drawable.ic_right_condition
                else R.drawable.ic_not_right_condition

                Image(
                    painter = painterResource(imageResource),
                    contentDescription = "",
                )

                val textColor = if (isPasswordNoWhitespace) CareTheme.colors.green
                else CareTheme.colors.red

                Text(
                    text = "공백 문자 사용 금지",
                    style = CareTheme.typography.body3,
                    color = textColor,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val imageResource = if (isPasswordNoSequentialChars) R.drawable.ic_right_condition
                else R.drawable.ic_not_right_condition

                Image(
                    painter = painterResource(imageResource),
                    contentDescription = "",
                )

                val textColor = if (isPasswordNoSequentialChars) CareTheme.colors.green
                else CareTheme.colors.red

                Text(
                    text = "연속된 문자 3개 이상 사용 금지",
                    style = CareTheme.typography.body3,
                    color = textColor,
                )
            }
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
                onDone = {
                    if (isPasswordLengthValid &&
                        isPasswordContainsLetterAndDigit &&
                        isPasswordNoWhitespace &&
                        isPasswordNoSequentialChars &&
                        newPasswordForConfirm.isNotBlank() && newPassword == newPasswordForConfirm
                    ) {
                        generateNewPassword()
                    }
                },
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
            enable = isPasswordLengthValid &&
                    isPasswordContainsLetterAndDigit &&
                    isPasswordNoWhitespace &&
                    isPasswordNoSequentialChars &&
                    (newPasswordForConfirm.isNotBlank() && newPassword == newPasswordForConfirm),
            onClick = generateNewPassword,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }
}