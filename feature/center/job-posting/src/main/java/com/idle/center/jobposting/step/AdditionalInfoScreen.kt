package com.idle.center.jobposting.step

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.idle.center.jobposting.JobPostingStep
import com.idle.center.jobposting.JobPostingStep.ADDITIONAL_INFO
import com.idle.compose.JobPostingBottomSheetType
import com.idle.designresource.R
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
            .verticalScroll(scrollState),
    ) {
        Text(
            text = stringResource(id = R.string.additional_info_title),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.experience_preference)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareChipBasic(
                    text = stringResource(id = R.string.beginner_possible),
                    onClick = { onExperiencePreferredChanged(false) },
                    enable = isExperiencePreferred == false,
                    modifier = Modifier.width(104.dp),
                )

                CareChipBasic(
                    text = stringResource(id = R.string.experience_preferred),
                    onClick = { onExperiencePreferredChanged(true) },
                    enable = isExperiencePreferred == true,
                    modifier = Modifier.width(104.dp),
                )
            }
        }

        LabeledContent(
            subtitle = buildAnnotatedString {
                append(stringResource(id = R.string.apply_method))
                withStyle(
                    style = SpanStyle(
                        color = CareTheme.colors.gray300,
                        fontSize = CareTheme.typography.caption.fontSize,
                        fontFamily = PretendardMedium,
                    )
                ) {
                    append(stringResource(id = R.string.apply_method_hint))
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
            subtitle = stringResource(id = R.string.apply_deadline)
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

                if (applyDeadlineType == ApplyDeadlineType.LIMITED) {
                    CareClickableTextField(
                        value = applyDeadline?.toString() ?: "",
                        hint = stringResource(id = R.string.apply_deadline_hint),
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
            text = stringResource(id = R.string.next),
            enable = isExperiencePreferred != null && applyMethod.isNotEmpty() && applyDeadlineType != null && (applyDeadlineType == ApplyDeadlineType.LIMITED && applyDeadline != null),
            onClick = { setJobPostingStep(JobPostingStep.findStep(ADDITIONAL_INFO.step + 1)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }
}