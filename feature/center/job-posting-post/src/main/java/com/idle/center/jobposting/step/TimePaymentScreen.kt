package com.idle.center.jobposting.step

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.idle.center.jobposting.JobPostingStep
import com.idle.center.jobposting.JobPostingStep.TIME_PAYMENT
import com.idle.center.jobposting.LogJobPostingStep
import com.idle.compose.JobPostingBottomSheetType
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareChipBasic
import com.idle.designsystem.compose.component.CareChipShort
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.jobposting.DayOfWeek
import com.idle.domain.model.jobposting.PayType

@Composable
internal fun TimePaymentScreen(
    weekDays: Set<DayOfWeek>,
    workStartTime: String,
    workEndTime: String,
    payType: PayType?,
    payAmount: String,
    isMinimumWageError: Boolean,
    setWeekDays: (DayOfWeek) -> Unit,
    onPayTypeChanged: (PayType) -> Unit,
    onPayAmountChanged: (String) -> Unit,
    setJobPostingStep: (JobPostingStep) -> Unit,
    showBottomSheet: (JobPostingBottomSheetType) -> Unit,
    showSnackBar: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = stringResource(id = R.string.time_payment_title),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.black,
            modifier = Modifier.padding(bottom = 28.dp),
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.work_days_subtitle),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                DayOfWeek.entries.forEach { day ->
                    CareChipShort(
                        text = day.displayName,
                        enable = day in weekDays,
                        onClick = { setWeekDays(day) },
                    )
                }
            }
        }

        LabeledContent(
            subtitle = stringResource(id = R.string.work_hours_subtitle),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                CareClickableTextField(
                    value = workStartTime,
                    hint = stringResource(id = R.string.work_start_time_hint),
                    onClick = { showBottomSheet(JobPostingBottomSheetType.WORK_START_TIME) },
                    leftComponent = {
                        Image(
                            painter = painterResource(com.idle.designresource.R.drawable.ic_arrow_down),
                            contentDescription = null,
                        )
                    },
                    modifier = Modifier.weight(1f),
                )

                CareClickableTextField(
                    value = workEndTime,
                    hint = stringResource(id = R.string.work_end_time_hint),
                    onClick = { showBottomSheet(JobPostingBottomSheetType.WORK_END_TIME) },
                    leftComponent = {
                        Image(
                            painter = painterResource(com.idle.designresource.R.drawable.ic_arrow_down),
                            contentDescription = null,
                        )
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        LabeledContent(
            subtitle = stringResource(id = R.string.pay),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(bottom = 12.dp),
                ) {
                    CareChipBasic(
                        text = stringResource(id = R.string.hourly),
                        onClick = { onPayTypeChanged(PayType.HOURLY) },
                        enable = payType == PayType.HOURLY,
                        modifier = Modifier.weight(1f),
                    )

                    CareChipBasic(
                        text = stringResource(id = R.string.weekly),
                        onClick = { onPayTypeChanged(PayType.WEEKLY) },
                        enable = payType == PayType.WEEKLY,
                        modifier = Modifier.weight(1f),
                    )

                    CareChipBasic(
                        text = stringResource(id = R.string.monthly),
                        onClick = { onPayTypeChanged(PayType.MONTHLY) },
                        enable = payType == PayType.MONTHLY,
                        modifier = Modifier.weight(1f),
                    )
                }

                CareTextField(
                    value = payAmount,
                    hint = stringResource(id = R.string.pay_amount_hint),
                    textStyle = CareTheme.typography.body2,
                    onValueChanged = onPayAmountChanged,
                    keyboardType = KeyboardType.Number,
                    isError = isMinimumWageError,
                    leftComponent = {
                        Text(
                            text = stringResource(id = R.string.currency_unit),
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray500,
                        )
                    },
                    onDone = {
                        if (weekDays.isNotEmpty() && workStartTime.isNotBlank() && workEndTime.isNotBlank() && payType != null && payAmount.isNotBlank()) {
                            if ((payAmount.toIntOrNull() ?: return@CareTextField) < 9860) {
                                return@CareTextField
                            }

                            setJobPostingStep(JobPostingStep.findStep(TIME_PAYMENT.step + 1))
                        }
                    },
                    modifier = Modifier.padding(bottom = 2.dp),
                )

                Text(
                    text = if (isMinimumWageError) stringResource(R.string.minimum_wage_description) else "",
                    style = CareTheme.typography.caption1,
                    color = CareTheme.colors.red,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.next),
            enable = weekDays.isNotEmpty() &&
                    workStartTime.isNotBlank() &&
                    workEndTime.isNotBlank() &&
                    payType != null &&
                    payAmount.isNotBlank()
                    && !isMinimumWageError,
            onClick = {
                if ((payAmount.toIntOrNull() ?: return@CareButtonLarge) < 9860) {
                    return@CareButtonLarge
                }

                setJobPostingStep(JobPostingStep.findStep(TIME_PAYMENT.step + 1))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }

    LogJobPostingStep(step = TIME_PAYMENT)
}
