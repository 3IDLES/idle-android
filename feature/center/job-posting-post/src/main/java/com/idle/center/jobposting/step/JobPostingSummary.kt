package com.idle.center.jobposting.step

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.idle.center.jobposting.JobPostingStep
import com.idle.center.jobposting.JobPostingStep.SUMMARY
import com.idle.center.jobposting.LogJobPostingStep
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareButtonRound
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.DayOfWeek
import com.idle.domain.model.jobposting.LifeAssistance
import com.idle.domain.model.jobposting.MentalStatus
import com.idle.domain.model.jobposting.PayType
import com.idle.worker.job.posting.detail.center.SummaryScreen
import java.time.LocalDate

@Composable
internal fun JobPostingSummaryScreen(
    weekDays: Set<DayOfWeek>,
    workStartTime: String,
    workEndTime: String,
    payType: PayType?,
    payAmount: String,
    roadNameAddress: String,
    lotNumberAddress: String,
    clientName: String,
    gender: Gender,
    birthYear: String,
    weight: String,
    careLevel: String,
    mentalStatus: MentalStatus,
    disease: String,
    isMealAssistance: Boolean?,
    isBowelAssistance: Boolean?,
    isWalkingAssistance: Boolean?,
    lifeAssistance: Set<LifeAssistance>,
    extraRequirement: String,
    isExperiencePreferred: Boolean?,
    applyMethod: Set<ApplyMethod>,
    applyDeadline: LocalDate?,
    setEditState: (Boolean) -> Unit,
    setJobPostingStep: (JobPostingStep) -> Unit,
    postJobPosting: () -> Unit,
) {
    Scaffold(
        topBar = {
            CareSubtitleTopBar(
                title = stringResource(id = R.string.post_job_posting),
                onNavigationClick = {
                    setJobPostingStep(JobPostingStep.findStep(SUMMARY.step - 1))
                },
                leftComponent = {
                    CareButtonRound(
                        text = stringResource(id = R.string.edit_job_posting_button),
                        onClick = { setEditState(true) },
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, end = 20.dp, bottom = 12.dp),
            )
        },
        containerColor = CareTheme.colors.white000,
    ) { paddingValue ->
        SummaryScreen(
            weekDays = weekDays,
            workStartTime = workStartTime,
            workEndTime = workEndTime,
            payType = payType,
            payAmount = payAmount,
            roadNameAddress = roadNameAddress,
            lotNumberAddress = lotNumberAddress,
            clientName = clientName,
            gender = gender,
            birthYear = birthYear,
            weight = weight,
            careLevel = careLevel,
            mentalStatus = mentalStatus,
            disease = disease,
            isMealAssistance = isMealAssistance,
            isBowelAssistance = isBowelAssistance,
            isWalkingAssistance = isWalkingAssistance,
            lifeAssistance = lifeAssistance,
            extraRequirement = extraRequirement,
            isExperiencePreferred = isExperiencePreferred,
            applyMethod = applyMethod,
            applyDeadline = applyDeadline,
            onBackPressed = {
                setJobPostingStep(
                    JobPostingStep.findStep(
                        SUMMARY.step - 1
                    )
                )
            },
            titleComponent = {
                Text(
                    text = stringResource(id = R.string.summary_title),
                    style = CareTheme.typography.heading2,
                    color = CareTheme.colors.gray900,
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 8.dp
                    ),
                )
            },
            bottomComponent = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.post_job_posting_note),
                        style = CareTheme.typography.body3,
                        color = CareTheme.colors.gray300,
                    )

                    CareButtonLarge(
                        text = stringResource(id = R.string.confirm),
                        onClick = postJobPosting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                    )
                }
            },
            modifier = Modifier
                .padding(paddingValue)
                .padding(top = 24.dp),
        )
    }

    LogJobPostingStep(step = SUMMARY)
}