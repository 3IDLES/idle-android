package com.idle.signup.center.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.idle.center.job.posting.R
import com.idle.center.jobposting.JobPostingStep
import com.idle.center.jobposting.JobPostingStep.CUSTOMER_REQUIREMENT
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareChipBasic
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.job.LifeAssistance

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun CustomerRequirementScreen(
    isMealAssistance: Boolean?,
    isBowelAssistance: Boolean?,
    isWalkingAssistance: Boolean?,
    lifeAssistance: Set<LifeAssistance>,
    speciality: String,
    setMealAssistance: (Boolean) -> Unit,
    setBowelAssistance: (Boolean) -> Unit,
    setWalkingAssistance: (Boolean) -> Unit,
    setLifeAssistance: (LifeAssistance) -> Unit,
    setSpeciality: (String) -> Unit,
    setJobPostingStep: (JobPostingStep) -> Unit,
) {
    val scrollState = rememberScrollState()

    BackHandler { setJobPostingStep(JobPostingStep.findStep(CUSTOMER_REQUIREMENT.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier.fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 30.dp),
    ) {
        Text(
            text = "고객 요구사항을 입력해주세요.",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        LabeledContent(
            subtitle = "식사보조"
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareChipBasic(
                    text = stringResource(R.string.necessary),
                    onClick = { setMealAssistance(true) },
                    enable = isMealAssistance == true,
                    modifier = Modifier.width(104.dp),
                )

                CareChipBasic(
                    text = stringResource(R.string.unnecessary),
                    onClick = { setMealAssistance(false) },
                    enable = isMealAssistance == false,
                    modifier = Modifier.width(104.dp),
                )
            }
        }

        LabeledContent(
            subtitle = "배변보조"
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareChipBasic(
                    text = stringResource(R.string.necessary),
                    onClick = { setBowelAssistance(true) },
                    enable = isBowelAssistance == true,
                    modifier = Modifier.width(104.dp),
                )

                CareChipBasic(
                    text = stringResource(R.string.unnecessary),
                    onClick = { setBowelAssistance(false) },
                    enable = isBowelAssistance == false,
                    modifier = Modifier.width(104.dp),
                )
            }
        }

        LabeledContent(
            subtitle = "이동보조"
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareChipBasic(
                    text = stringResource(R.string.necessary),
                    onClick = { setWalkingAssistance(true) },
                    enable = isWalkingAssistance == true,
                    modifier = Modifier.width(104.dp),
                )

                CareChipBasic(
                    text = stringResource(R.string.unnecessary),
                    onClick = { setWalkingAssistance(false) },
                    enable = isWalkingAssistance == false,
                    modifier = Modifier.width(104.dp),
                )
            }
        }

        LabeledContent(
            subtitle = "일상보조"
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                maxItemsInEachRow = 3,
                modifier = Modifier.fillMaxWidth()
            ) {
                LifeAssistance.entries.forEach { assistance ->
                    CareChipBasic(
                        text = assistance.displayName,
                        onClick = { setLifeAssistance(assistance) },
                        enable = assistance in lifeAssistance,
                        modifier = Modifier.width(104.dp),
                    )
                }
            }
        }

        LabeledContent(
            subtitle = "이외에 요구사항이 있다면 적어주세요."
        ) {
            CareTextFieldLong(
                value = speciality,
                hint = "추가적으로 요구사항이 있다면 작성해주세요.(예: 어쩌고저쩌고)",
                onValueChanged = setSpeciality,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            enable = isMealAssistance != null && isBowelAssistance != null && isWalkingAssistance != null,
            onClick = { setJobPostingStep(JobPostingStep.findStep(CUSTOMER_REQUIREMENT.step + 1)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}