package com.idle.signup.center.process

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareButtonSmall
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.center.CenterSignUpProcess

@Composable
internal fun IdPasswordScreen(
    centerId: String,
    centerPassword: String,
    centerPasswordForConfirm: String,
    onCenterIdChanged: (String) -> Unit,
    onCenterPasswordChanged: (String) -> Unit,
    onCenterPasswordForConfirmChanged: (String) -> Unit,
    setSignUpProcess: (CenterSignUpProcess) -> Unit,
    signUpCenter: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler { setSignUpProcess(CenterSignUpProcess.BUSINESS_REGISTRAION_NUMBER) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "아이디와 비밀번호를 설정해주세요",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color = CareTheme.colors.gray500)
                    ) {
                        append("아이디 설정 ")
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
                style = CareTheme.typography.subtitle4,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareTextField(
                    value = centerId,
                    hint = "아이디를 입력해주세요.",
                    onValueChanged = onCenterIdChanged,
                    onDone = { },
                    modifier = Modifier.weight(1f)
                        .focusRequester(focusRequester),
                )

                CareButtonSmall(
                    enable = centerId.isNotBlank(),
                    text = "중복 확인",
                    onClick = { },
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = buildAnnotatedString {
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
                style = CareTheme.typography.subtitle4,
            )

            CareTextField(
                value = centerPassword,
                hint = "비밀번호를 입력해주세요.",
                onValueChanged = onCenterPasswordChanged,
                onDone = { },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = "비밀번호 확인",
                style = CareTheme.typography.subtitle4,
                color = CareTheme.colors.gray500,
            )


            CareTextField(
                value = centerPasswordForConfirm,
                hint = "비밀번호를 한번 더 입력해주세요.",
                onValueChanged = onCenterPasswordForConfirmChanged,
                onDone = { },
                modifier = Modifier.fillMaxWidth(),
            )
        }


        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "완료",
            enable = centerPasswordForConfirm.isNotBlank(),
            onClick = signUpCenter,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}