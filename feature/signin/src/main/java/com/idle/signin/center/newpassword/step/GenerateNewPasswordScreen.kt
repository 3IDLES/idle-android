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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = stringResource(id = R.string.new_password_title),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.black,
        )

        LabeledContent(
            subtitle = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(color = CareTheme.colors.gray500)
                ) {
                    append("비밀번호 설정 ")
                }
                withStyle(
                    style = SpanStyle(
                        color = CareTheme.colors.gray300,
                        fontSize = 12.sp,
                    )
                ) {
                    append(stringResource(id = R.string.password_conditions))
                }
            },
            modifier = Modifier.fillMaxWidth(),
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

        LabeledContent(
            subtitle = stringResource(id = R.string.confirm_password),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = newPasswordForConfirm,
                hint = stringResource(id = R.string.confirm_password_hint),
                onValueChanged = onNewPasswordForConfirmChanged,
                visualTransformation = PasswordVisualTransformation(),
                onDone = {
                    if (newPasswordForConfirm.isNotBlank() && newPassword == newPasswordForConfirm) {
                        generateNewPassword()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.change_password),
            enable = newPasswordForConfirm.isNotBlank() && newPassword == newPasswordForConfirm,
            onClick = generateNewPassword,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }
}