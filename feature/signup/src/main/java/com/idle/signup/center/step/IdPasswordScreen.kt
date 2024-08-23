package com.idle.signup.center.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.idle.signin.center.CenterSignUpStep
import com.idle.signin.center.CenterSignUpStep.ID_PASSWORD

@Composable
internal fun IdPasswordScreen(
    centerId: String,
    centerIdResult: Boolean,
    centerPassword: String,
    centerPasswordForConfirm: String,
    onCenterIdChanged: (String) -> Unit,
    onCenterPasswordChanged: (String) -> Unit,
    onCenterPasswordForConfirmChanged: (String) -> Unit,
    setSignUpStep: (CenterSignUpStep) -> Unit,
    signUpCenter: () -> Unit,
    validateIdentifier: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val idValidationResult = validateId(centerId)
    val passwordValidationResult = validatePassword(centerPassword)

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(centerIdResult) {
        if (centerIdResult) {
            focusManager.moveFocus(FocusDirection.Down)
        }
    }

    BackHandler { setSignUpStep(CenterSignUpStep.findStep(ID_PASSWORD.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.set_id_and_password),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        LabeledContent(
            subtitle = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(color = CareTheme.colors.gray500)
                ) {
                    append(stringResource(id = R.string.set_id_label))
                }
                withStyle(
                    style = SpanStyle(
                        color = CareTheme.colors.gray300,
                        fontSize = 12.sp,
                    )
                ) {
                    append(stringResource(id = R.string.id_conditions))
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareTextField(
                    value = centerId,
                    hint = stringResource(id = R.string.id_hint),
                    onValueChanged = onCenterIdChanged,
                    supportingText = if (centerIdResult) stringResource(id = R.string.id_available) else "",
                    onDone = { if (idValidationResult.isValid) validateIdentifier() },
                    modifier = Modifier
                        .weight(1f)
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
                        text = stringResource(id = R.string.check_duplicate),
                        style = CareTheme.typography.subtitle4,
                        color = CareTheme.colors.gray300,
                    )
                }
            }
        }

        LabeledContent(
            subtitle = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(color = CareTheme.colors.gray500)
                ) {
                    append(stringResource(id = R.string.set_password_label))
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
                value = centerPassword,
                hint = stringResource(id = R.string.password_hint),
                visualTransformation = PasswordVisualTransformation(),
                supportingText = if (passwordValidationResult.isValid) stringResource(id = R.string.password_available) else "",
                onValueChanged = onCenterPasswordChanged,
                onDone = { if (centerPassword.length >= 8) focusManager.moveFocus(FocusDirection.Down) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        LabeledContent(
            subtitle = stringResource(id = R.string.confirm_password),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = centerPasswordForConfirm,
                hint = stringResource(id = R.string.confirm_password_hint),
                visualTransformation = PasswordVisualTransformation(),
                supportingText = if (centerPassword != centerPasswordForConfirm) stringResource(
                    id = R.string.password_mismatch
                ) else "",
                onValueChanged = onCenterPasswordForConfirmChanged,
                onDone = {
                    if (idValidationResult.isValid && passwordValidationResult.isValid &&
                        (centerPassword == centerPasswordForConfirm)
                    ) {
                        signUpCenter()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.complete),
            enable = idValidationResult.isValid && passwordValidationResult.isValid &&
                    (centerPassword == centerPasswordForConfirm),
            onClick = signUpCenter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
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