package com.idle.signup.center.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonMedium
import com.idle.designsystem.compose.component.CareButtonSmall
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.ConditionRow
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signup.LogCenterSignUpStep
import com.idle.signup.center.CenterSignUpStep
import com.idle.signup.center.CenterSignUpStep.ID_PASSWORD

@Composable
internal fun IdPasswordScreen(
    centerId: String,
    centerIdResult: Boolean?,
    centerPassword: String,
    centerPasswordForConfirm: String,
    isIdValid: Boolean,
    isPasswordLengthValid: Boolean,
    isPasswordContainsLetterAndDigit: Boolean,
    isPasswordNoWhitespace: Boolean,
    isPasswordNoSequentialChars: Boolean,
    isPasswordValid: Boolean,
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
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(centerIdResult) {
        if (centerIdResult == true) {
            focusManager.moveFocus(FocusDirection.Down)
        }
    }

    BackHandler { setSignUpStep(CenterSignUpStep.findStep(ID_PASSWORD.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        Text(
            text = stringResource(id = R.string.set_id_and_password),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.black,
            modifier = Modifier.padding(bottom = 28.dp),
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.set_id_label),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
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
                    isError = centerIdResult == false,
                    supportingText = when (centerIdResult) {
                        false -> stringResource(R.string.id_disavailable)
                        true -> stringResource(id = R.string.id_available)
                        else -> ""
                    },
                    onDone = { if (isIdValid) validateIdentifier() },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                )

                CareButtonSmall(
                    text = stringResource(id = R.string.check_duplicate),
                    onClick = {
                        validateIdentifier()
                        keyboardController?.hide()
                    },
                    enable = isIdValid && centerIdResult != false,
                )
            }
        }

        Text(
            text = stringResource(id = R.string.id_description),
            style = CareTheme.typography.body3,
            color = CareTheme.colors.gray500,
            modifier = Modifier.padding(bottom = 6.dp),
        )

        ConditionRow(
            isValid = isIdValid,
            conditionText = stringResource(R.string.condition_id_length),
            modifier = Modifier.padding(bottom = 24.dp),
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.set_password_label),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        ) {
            CareTextField(
                value = centerPassword,
                hint = stringResource(id = R.string.password_hint),
                visualTransformation = PasswordVisualTransformation(),
                onValueChanged = onCenterPasswordChanged,
                onDone = { if (centerPassword.length >= 8) focusManager.moveFocus(FocusDirection.Down) },
                modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = centerPasswordForConfirm,
                hint = stringResource(id = R.string.confirm_password_hint),
                visualTransformation = PasswordVisualTransformation(),
                supportingText = if (centerPasswordForConfirm.isNotBlank() && centerPassword != centerPasswordForConfirm)
                    stringResource(id = R.string.password_mismatch) else "",
                isError = centerPasswordForConfirm.isNotBlank() && centerPassword != centerPasswordForConfirm,
                onValueChanged = onCenterPasswordForConfirmChanged,
                onDone = { if (isIdValid && isPasswordValid) signUpCenter() },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 28.dp),
        ) {
            CareButtonMedium(
                text = stringResource(id = R.string.previous),
                textColor = CareTheme.colors.gray300,
                containerColor = CareTheme.colors.white000,
                border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray200),
                onClick = { setSignUpStep(CenterSignUpStep.findStep(ID_PASSWORD.step - 1)) },
                modifier = Modifier.weight(1f),
            )

            CareButtonMedium(
                text = stringResource(id = R.string.complete),
                enable = isIdValid && isPasswordValid,
                onClick = signUpCenter,
                modifier = Modifier.weight(1f),
            )
        }
    }

    LogCenterSignUpStep(ID_PASSWORD)
}
