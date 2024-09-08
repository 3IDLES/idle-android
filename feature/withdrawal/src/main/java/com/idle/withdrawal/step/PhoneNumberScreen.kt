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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonMedium
import com.idle.designsystem.compose.component.CareButtonSmall
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.withdrawal.LogWithdrawalStep
import com.idle.withdrawal.WithdrawalStep

@Composable
internal fun PhoneNumberScreen(
    timerMinute: String,
    timerSeconds: String,
    isConfirmAuthCode: Boolean,
    onPhoneNumberChanged: (String) -> Unit,
    onAuthCodeChanged: (String) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
    setWithdrawalStep: (WithdrawalStep) -> Unit,
    withdrawal: () -> Unit,
    navigateToSetting: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var authCode by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler { setWithdrawalStep(WithdrawalStep.findStep(WithdrawalStep.PHONE_NUMBER.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = stringResource(id = R.string.withdrawal_phone_number_verification_title),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
            modifier = Modifier.padding(bottom = 36.dp),
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.phone_number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                CareTextField(
                    value = phoneNumber,
                    hint = stringResource(id = R.string.phone_number_hint),
                    onValueChanged = {
                        onPhoneNumberChanged(it)
                        phoneNumber = it
                    },
                    readOnly = (timerMinute != "" && timerSeconds != ""),
                    onDone = { if (phoneNumber.length == 11) sendPhoneNumber() },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                )

                CareButtonSmall(
                    enable = phoneNumber.length == 11 &&
                            !(timerMinute != "" && timerSeconds != ""),
                    text = stringResource(id = R.string.verification),
                    onClick = sendPhoneNumber,
                )
            }
        }

        if (timerMinute.isNotBlank()) {
            LabeledContent(
                subtitle = stringResource(id = R.string.confirm_code),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        CareTextField(
                            value = authCode,
                            hint = "",
                            onValueChanged = {
                                onAuthCodeChanged(it)
                                authCode = it
                            },
                            readOnly = !(timerMinute != "" && timerSeconds != "") || isConfirmAuthCode,
                            onDone = { if (authCode.isNotBlank()) confirmAuthCode() },
                            leftComponent = {
                                if (timerMinute != "" && timerSeconds != "") {
                                    Text(
                                        text = "$timerMinute:$timerSeconds",
                                        style = CareTheme.typography.body3,
                                        color = if (!isConfirmAuthCode) CareTheme.colors.gray500 else CareTheme.colors.gray200,
                                    )
                                }
                            },
                        )

                        Text(
                            text = if (isConfirmAuthCode) stringResource(id = R.string.confirm_success)
                            else "",
                            style = CareTheme.typography.caption,
                            color = CareTheme.colors.gray300,
                        )
                    }

                    CareButtonSmall(
                        enable = authCode.isNotBlank() && !isConfirmAuthCode,
                        text = stringResource(id = R.string.confirm_short),
                        onClick = confirmAuthCode,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        ) {
            CareButtonMedium(
                text = stringResource(id = R.string.cancel),
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
                enable = isConfirmAuthCode,
                onClick = withdrawal,
                modifier = Modifier.weight(1f),
            )
        }
    }

    LogWithdrawalStep(step = WithdrawalStep.PHONE_NUMBER)
}