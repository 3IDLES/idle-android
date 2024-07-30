package com.idle.signin.center.newpassword.step

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler { setNewPasswordProcess(PHONE_NUMBER) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "새로운 비밀번호를 입력해주세요",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
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
                    append("(영문+숫자 조합 10자리 이상 등 조건)")
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = newPassword,
                hint = "비밀번호를 입력해주세요.",
                onValueChanged = onNewPasswordChanged,
                onDone = { },
                modifier = Modifier.fillMaxWidth()
                    .focusRequester(focusRequester),
            )
        }

        LabeledContent(
            subtitle = "비밀번호 확인",
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = newPasswordForConfirm,
                hint = "비밀번호를 한번 더 입력해주세요.",
                onValueChanged = onNewPasswordForConfirmChanged,
                onDone = { },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "비밀번호 변경",
            enable = newPasswordForConfirm.isNotBlank(),
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}