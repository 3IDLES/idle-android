package com.idle.signup.center.step

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
import androidx.compose.ui.unit.dp
import com.idle.center.jobposting.JobPostingStep
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareChipBasic
import com.idle.designsystem.compose.component.CareChipShort
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import java.time.DayOfWeek

@Composable
internal fun TimePaymentScreen(
    setJobPostingStep: (JobPostingStep) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier.fillMaxSize()
            .padding(bottom = 30.dp),
    ) {
        Text(
            text = "근무 시간 및 급여를 입력해주세요.",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        LabeledContent(
            subtitle = "근무 요일",
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                DayOfWeek.entries.forEach { day ->
                    val koreanDay = when (day) {
                        DayOfWeek.MONDAY -> "월"
                        DayOfWeek.TUESDAY -> "화"
                        DayOfWeek.WEDNESDAY -> "수"
                        DayOfWeek.THURSDAY -> "목"
                        DayOfWeek.FRIDAY -> "금"
                        DayOfWeek.SATURDAY -> "토"
                        DayOfWeek.SUNDAY -> "일"
                    }

                    CareChipShort(
                        text = koreanDay,
                        onClick = {},
                        enable = false,
                    )
                }
            }
        }


        LabeledContent(
            subtitle = "근무 시간",
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                CareClickableTextField(
                    value = "",
                    hint = "시작 시간",
                    onClick = {},
                    leftComponent = {
                        Image(
                            painter = painterResource(com.idle.designresource.R.drawable.ic_arrow_down),
                            contentDescription = null,
                        )
                    },
                    modifier = Modifier.weight(1f),
                )

                CareClickableTextField(
                    value = "",
                    hint = "종료 시간",
                    onClick = {},
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
            subtitle = "급여",
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    CareChipBasic(
                        text = "시급",
                        onClick = {},
                        enable = false,
                        modifier = Modifier.weight(1f),
                    )

                    CareChipBasic(
                        text = "주급",
                        onClick = {},
                        enable = true,
                        modifier = Modifier.weight(1f),
                    )

                    CareChipBasic(
                        text = "월급",
                        onClick = {},
                        enable = false,
                        modifier = Modifier.weight(1f),
                    )
                }

                CareTextField(
                    value = "500,000",
                    hint = "",
                    textStyle = CareTheme.typography.body2,
                    onValueChanged = {},
                    leftComponent = {
                        Text(
                            text = "원",
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray500,
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            onClick = { setJobPostingStep(JobPostingStep.findStep(JobPostingStep.TIMEPAYMENT.step + 1)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}