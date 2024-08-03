package com.idle.signup.center.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.idle.center.jobposting.JobPostingStep
import com.idle.center.jobposting.JobPostingStep.ADDITIONAL_INFO
import com.idle.compose.JobPostingBottomSheetType
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareChipBasic
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.designsystem.compose.foundation.PretendardMedium
import com.idle.domain.model.job.ApplyDeadlineType
import com.idle.domain.model.job.ApplyMethod
import java.time.LocalDate

@Composable
internal fun AdditionalInfoScreen(
    isExperiencePreferred: Boolean?,
    applyMethod: Set<ApplyMethod>,
    applyDeadlineType: ApplyDeadlineType?,
    applyDeadline: LocalDate?,
    onExperiencePreferredChanged: (Boolean) -> Unit,
    onApplyMethodChanged: (ApplyMethod) -> Unit,
    onApplyDeadlineTypeChanged: (ApplyDeadlineType) -> Unit,
    setJobPostingStep: (JobPostingStep) -> Unit,
    showBottomSheet: (JobPostingBottomSheetType) -> Unit,
) {
    val scrollState = rememberScrollState()

    BackHandler { setJobPostingStep(JobPostingStep.findStep(ADDITIONAL_INFO.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 30.dp),
    ) {
        Text(
            text = "추가 지원정보를 입력해주세요.",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        LabeledContent(
            subtitle = "경력 우대 여부"
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareChipBasic(
                    text = "초보 가능",
                    onClick = { onExperiencePreferredChanged(false) },
                    enable = isExperiencePreferred == false,
                    modifier = Modifier.width(104.dp),
                )

                CareChipBasic(
                    text = "경력 우대",
                    onClick = { onExperiencePreferredChanged(true) },
                    enable = isExperiencePreferred == true,
                    modifier = Modifier.width(104.dp),
                )
            }
        }

        LabeledContent(
            subtitle = buildAnnotatedString {
                append("지원 방법 ")
                withStyle(
                    style = SpanStyle(
                        color = CareTheme.colors.gray300,
                        fontSize = CareTheme.typography.caption.fontSize,
                        fontFamily = PretendardMedium,
                    )
                ) {
                    append("(다중 선택 가능)")
                }
            },
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                ApplyMethod.entries.forEach { method ->
                    CareChipBasic(
                        text = method.displayName,
                        onClick = { onApplyMethodChanged(method) },
                        enable = method in applyMethod,
                        modifier = Modifier.width(104.dp),
                    )
                }
            }
        }

        LabeledContent(
            subtitle = "접수 마감일"
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ApplyDeadlineType.entries.forEach { type ->
                        CareChipBasic(
                            text = type.displayName,
                            onClick = { onApplyDeadlineTypeChanged(type) },
                            enable = applyDeadlineType == type,
                            modifier = Modifier.width(104.dp),
                        )
                    }
                }

                if (applyDeadlineType == ApplyDeadlineType.DEFINITE) {
                    CareClickableTextField(
                        value = applyDeadline?.toString() ?: "",
                        hint = "날짜를 선택해주세요.",
                        leftComponent = {
                            Image(
                                painter = painterResource(com.idle.designresource.R.drawable.ic_calendar),
                                contentDescription = null,
                            )
                        },
                        onClick = { showBottomSheet(JobPostingBottomSheetType.POST_DEAD_LINE) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            enable = isExperiencePreferred != null && applyMethod.isNotEmpty() && applyDeadlineType != null && (applyDeadlineType == ApplyDeadlineType.DEFINITE && applyDeadline != null),
            onClick = { setJobPostingStep(JobPostingStep.findStep(ADDITIONAL_INFO.step + 1)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}