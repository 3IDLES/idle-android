package com.idle.signup.center.process

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.center.CenterSignUpProcess

@Composable
internal fun IdPasswordScreen(
    centerId: String,
    centerIdResult: Boolean,
    centerPassword: String,
    centerPasswordForConfirm: String,
    onCenterIdChanged: (String) -> Unit,
    onCenterPasswordChanged: (String) -> Unit,
    onCenterPasswordForConfirmChanged: (String) -> Unit,
    setSignUpProcess: (CenterSignUpProcess) -> Unit,
    signUpCenter: () -> Unit,
    validateIdentifier: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val idValidationResult = validateId(centerId)
    val passwordValidationResult = validatePassword(centerPassword)

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler { setSignUpProcess(CenterSignUpProcess.BUSINESS_REGISTRATION_NUMBER) }

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
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareTextField(
                    value = centerId,
                    hint = "아이디를 입력해주세요.",
                    onValueChanged = onCenterIdChanged,
                    supportingText = if (centerIdResult) "사용 가능한 아이디 입니다" else "",
                    onDone = { if (idValidationResult.isValid) validateIdentifier() },
                    modifier = Modifier.weight(1f)
                        .focusRequester(focusRequester),
                )

                Button(
                    onClick = {
                        validateIdentifier()
                        keyboardController?.hide()
                    },
                    enabled = centerId.isNotBlank(),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(10.dp),
                    colors = ButtonColors(
                        containerColor = CareTheme.colors.white000,
                        contentColor = CareTheme.colors.white000,
                        disabledContentColor = CareTheme.colors.white000,
                        disabledContainerColor = CareTheme.colors.white000,
                    ),
                    border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray100),
                    modifier = Modifier.size(width = 76.dp, height = 44.dp),
                ) {
                    Text(
                        text = "중복 확인",
                        style = CareTheme.typography.subtitle4,
                        color = CareTheme.colors.gray300,
                    )
                }
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
                visualTransformation = PasswordVisualTransformation(),
                supportingText = if (passwordValidationResult.isValid) "사용 가능한 비밀번호 입니다." else "",
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
                visualTransformation = PasswordVisualTransformation(),
                supportingText = if (centerPassword != centerPasswordForConfirm) "비밀번호가 일치하지 않습니다." else "",
                onValueChanged = onCenterPasswordForConfirmChanged,
                onDone = { },
                modifier = Modifier.fillMaxWidth(),
            )
        }


        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "완료",
            enable = idValidationResult.isValid && passwordValidationResult.isValid &&
                    (centerPassword == centerPasswordForConfirm),
            onClick = signUpCenter,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private fun validateId(id: String): ValidationResult {
    if (id.length !in 6..20) return ValidationResult(false, "아이디는 6자 이상 20자 이하이어야 합니다.")
    if (!id.matches(Regex("^[A-Za-z0-9]+$"))) return ValidationResult(
        false,
        "아이디는 영문 대소문자와 숫자만 사용 가능합니다."
    )
    return ValidationResult(true, "유효한 아이디입니다.")
}

private fun validatePassword(password: String): ValidationResult {
    if (password.length !in 8..20) return ValidationResult(false, "비밀번호는 8자 이상 20자 이하이어야 합니다.")
    if (!password.matches(Regex("^[A-Za-z0-9!@#\$%^&*()_+=-]+$"))) return ValidationResult(
        false,
        "비밀번호는 영문, 숫자 및 특수문자만 사용 가능합니다."
    )
    if (!password.matches(Regex(".*[A-Za-z].*")) || !password.matches(Regex(".*\\d.*"))) return ValidationResult(
        false,
        "비밀번호는 최소 하나의 영문자와 숫자를 포함해야 합니다."
    )
    if (password.contains(" ")) return ValidationResult(false, "비밀번호에 공백 문자를 사용할 수 없습니다.")
    if (containsSequentialCharacters(password)) return ValidationResult(
        false,
        "비밀번호에 연속된 문자를 3개 이상 사용할 수 없습니다."
    )

    return ValidationResult(true, "유효한 비밀번호입니다.")
}

private fun containsSequentialCharacters(password: String): Boolean {
    val limit = 3
    var maxSequence = 1

    for (i in 1 until password.length) {
        if (password[i] == password[i - 1]) {
            maxSequence += 1
            if (maxSequence >= limit) return true
        } else {
            maxSequence = 1
        }
    }
    return false
}

data class ValidationResult(val isValid: Boolean, val message: String)