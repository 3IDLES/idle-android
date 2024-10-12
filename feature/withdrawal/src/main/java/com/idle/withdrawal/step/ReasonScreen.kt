package com.idle.withdrawal.step

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonMedium
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.UserType
import com.idle.withdrawal.LogWithdrawalStep
import com.idle.withdrawal.WithdrawalReason
import com.idle.withdrawal.WithdrawalReason.NO_LONGER_OPERATING_CENTER
import com.idle.withdrawal.WithdrawalReason.NO_LONGER_WISH_TO_CONTINUE
import com.idle.withdrawal.WithdrawalStep

@Composable
internal fun ReasonScreen(
    userType: UserType,
    inconvenientReason: String,
    anotherPlatformReason: String,
    lackFeaturesReason: String,
    onReasonChanged: (WithdrawalReason) -> Unit,
    setWithdrawalStep: (WithdrawalStep) -> Unit,
    onInconvenientReasonChanged: (String) -> Unit,
    onAnotherPlatformReasonChanged: (String) -> Unit,
    onLackFeaturesReasonChanged: (String) -> Unit,
    navigateToSetting: () -> Unit,
) {
    val scrollState = rememberScrollState()
    var withdrawalReason by rememberSaveable { mutableStateOf<Set<WithdrawalReason>>(setOf()) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        Text(
            text = stringResource(id = R.string.withdrawal_reason_title),
            style = CareTheme.typography.heading1,
            color = CareTheme.colors.black,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Text(
            text = stringResource(id = R.string.withdrawal_reason_description),
            style = CareTheme.typography.body3,
            color = CareTheme.colors.gray300,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            WithdrawalReason.entries.forEach { reason ->
                if (userType == UserType.WORKER && reason == NO_LONGER_OPERATING_CENTER) return@forEach
                if (userType == UserType.CENTER && reason == NO_LONGER_WISH_TO_CONTINUE) return@forEach

                when (reason) {
                    WithdrawalReason.INCONVENIENT_PLATFORM_USE -> WithdrawalLongReasonItem(
                        text = reason.displayName,
                        checked = (reason in withdrawalReason),
                        description = "어떤 부분에서 불편함을 느끼셨나요?",
                        hint = "어떤 부분에서 불편함을 느끼셨나요? 보내주신 의견은 플랫폼 개선에 큰 도움이 됩니다!",
                        useReasonTextField = true,
                        reason = inconvenientReason,
                        onReasonChanged = onInconvenientReasonChanged,
                        onClick = {
                            onReasonChanged(reason)
                            withdrawalReason = withdrawalReason.toMutableSet().apply {
                                if (reason in this) remove(reason)
                                else add(reason)
                            }.toSet()
                        },
                    )

                    WithdrawalReason.USING_ANOTHER_PLATFORM -> WithdrawalReasonItem(
                        text = reason.displayName,
                        checked = (reason in withdrawalReason),
                        description = "사용하시는 플랫폼의 이름은 무엇인가요?",
                        hint = "어떤 플랫폼을 사용하시나요?",
                        reason = anotherPlatformReason,
                        useReasonTextField = true,
                        onReasonChanged = onAnotherPlatformReasonChanged,
                        onClick = {
                            onReasonChanged(reason)
                            withdrawalReason = withdrawalReason.toMutableSet().apply {
                                if (reason in this) remove(reason)
                                else add(reason)
                            }.toSet()
                        },
                    )

                    WithdrawalReason.LACK_OF_DESIRED_FEATURES -> WithdrawalLongReasonItem(
                        text = reason.displayName,
                        checked = (reason in withdrawalReason),
                        description = "어떤 기능이 필요하신가요?",
                        hint = "어떤 기능이 필요하신가요? 보내주신 의견은 개발 담당자에게 즉시 전달됩니다.",
                        reason = lackFeaturesReason,
                        useReasonTextField = true,
                        onReasonChanged = onLackFeaturesReasonChanged,
                        onClick = {
                            onReasonChanged(reason)
                            withdrawalReason = withdrawalReason.toMutableSet().apply {
                                if (reason in this) remove(reason)
                                else add(reason)
                            }.toSet()
                        },
                    )

                    else -> WithdrawalLongReasonItem(
                        text = reason.displayName,
                        checked = (reason in withdrawalReason),
                        onClick = {
                            onReasonChanged(reason)
                            withdrawalReason = withdrawalReason.toMutableSet().apply {
                                if (reason in this) remove(reason)
                                else add(reason)
                            }.toSet()
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(id = R.string.withdrawal_warning),
            style = CareTheme.typography.caption,
            color = CareTheme.colors.red,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 48.dp, bottom = 12.dp)
                .fillMaxWidth(),
        )

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
                enable = withdrawalReason.isNotEmpty() && withdrawalReason.all {
                    when (it) {
                        WithdrawalReason.LACK_OF_DESIRED_FEATURES -> lackFeaturesReason.isNotBlank()
                        WithdrawalReason.INCONVENIENT_PLATFORM_USE -> inconvenientReason.isNotBlank()
                        WithdrawalReason.USING_ANOTHER_PLATFORM -> anotherPlatformReason.isNotBlank()
                        else -> true
                    }
                },
                onClick = {
                    when (userType) {
                        UserType.WORKER -> setWithdrawalStep(WithdrawalStep.PHONE_NUMBER)
                        UserType.CENTER -> setWithdrawalStep(WithdrawalStep.PASSWORD)
                    }
                },
                modifier = Modifier.weight(1f),
            )
        }
    }

    LogWithdrawalStep(step = WithdrawalStep.REASON)
}

@Composable
private fun WithdrawalLongReasonItem(
    text: String,
    checked: Boolean,
    onClick: () -> Unit,
    useReasonTextField: Boolean = false,
    description: String = "",
    reason: String = "",
    hint: String = "",
    onReasonChanged: (String) -> Unit = {},
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (!checked) CareTheme.colors.white000
        else CareTheme.colors.orange500,
    )
    val borderColor by animateColorAsState(
        targetValue = if (!checked) CareTheme.colors.gray100
        else CareTheme.colors.orange500
    )

    Column(horizontalAlignment = Alignment.Start) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(2.dp),
                    )
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(2.dp),
                    )
                    .clickable { onClick() }
            ) {
                AnimatedVisibility(visible = checked) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = null
                    )
                }
            }

            Text(
                text = text,
                style = CareTheme.typography.body2,
                color = CareTheme.colors.gray500,
            )
        }

        if (useReasonTextField && checked) {
            Text(
                text = description,
                style = CareTheme.typography.subtitle4,
                color = CareTheme.colors.gray500,
                modifier = Modifier.padding(top = 12.dp, bottom = 6.dp),
            )

            CareTextFieldLong(
                value = reason,
                onValueChanged = onReasonChanged,
                hint = hint,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}

@Composable
private fun WithdrawalReasonItem(
    text: String,
    checked: Boolean,
    onClick: () -> Unit,
    useReasonTextField: Boolean = false,
    description: String = "",
    reason: String = "",
    hint: String = "",
    onReasonChanged: (String) -> Unit = {},
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (!checked) CareTheme.colors.white000
        else CareTheme.colors.orange500,
    )
    val borderColor by animateColorAsState(
        targetValue = if (!checked) CareTheme.colors.gray100
        else CareTheme.colors.orange500
    )

    Column(horizontalAlignment = Alignment.Start) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(2.dp),
                    )
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(2.dp),
                    )
                    .clickable { onClick() }
            ) {
                AnimatedVisibility(visible = checked) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = null
                    )
                }
            }

            Text(
                text = text,
                style = CareTheme.typography.body2,
                color = CareTheme.colors.gray500,
            )
        }

        if (useReasonTextField && checked) {
            Text(
                text = description,
                style = CareTheme.typography.subtitle4,
                color = CareTheme.colors.gray500,
                modifier = Modifier.padding(top = 12.dp, bottom = 6.dp),
            )

            CareTextField(
                value = reason,
                onValueChanged = onReasonChanged,
                hint = hint,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWithdrawalReasonItem() {
    CareTheme {
        WithdrawalLongReasonItem(
            text = "Sample Withdrawal Reason",
            checked = true,
            onClick = { /* No action for preview */ },
            useReasonTextField = true,
            description = "Please describe your reason:",
            reason = "",
            hint = "Enter your reason",
            onReasonChanged = {}
        )
    }
}