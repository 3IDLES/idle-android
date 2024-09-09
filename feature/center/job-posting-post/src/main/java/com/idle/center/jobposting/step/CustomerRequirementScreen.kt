package com.idle.center.jobposting.step

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.idle.center.jobposting.JobPostingStep
import com.idle.center.jobposting.JobPostingStep.CUSTOMER_INFORMATION
import com.idle.center.jobposting.JobPostingStep.CUSTOMER_REQUIREMENT
import com.idle.center.jobposting.LogJobPostingStep
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareButtonMedium
import com.idle.designsystem.compose.component.CareChipBasic
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.designsystem.compose.foundation.PretendardMedium
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.LifeAssistance
import com.idle.domain.model.jobposting.MentalStatus
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun CustomerRequirementScreen(
    isMealAssistance: Boolean?,
    isBowelAssistance: Boolean?,
    isWalkingAssistance: Boolean?,
    lifeAssistance: Set<LifeAssistance>,
    extraRequirement: String,
    onMealAssistanceChanged: (Boolean) -> Unit,
    onBowelAssistanceChanged: (Boolean) -> Unit,
    onWalkingAssistanceChanged: (Boolean) -> Unit,
    onLifeAssistanceChanged: (LifeAssistance) -> Unit,
    onExtraRequirementChanged: (String) -> Unit,
    setJobPostingStep: (JobPostingStep) -> Unit,
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    BackHandler { setJobPostingStep(JobPostingStep.findStep(CUSTOMER_REQUIREMENT.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        Text(
            text = stringResource(id = R.string.customer_requirement_title),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        LabeledContent(subtitle = stringResource(id = R.string.meal_assistance)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareChipBasic(
                    text = stringResource(R.string.necessary),
                    onClick = { onMealAssistanceChanged(true) },
                    enable = isMealAssistance == true,
                    modifier = Modifier.width(104.dp),
                )

                CareChipBasic(
                    text = stringResource(R.string.unnecessary),
                    onClick = { onMealAssistanceChanged(false) },
                    enable = isMealAssistance == false,
                    modifier = Modifier.width(104.dp),
                )
            }
        }

        LabeledContent(subtitle = stringResource(id = R.string.bowel_assistance)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareChipBasic(
                    text = stringResource(R.string.necessary),
                    onClick = { onBowelAssistanceChanged(true) },
                    enable = isBowelAssistance == true,
                    modifier = Modifier.width(104.dp),
                )

                CareChipBasic(
                    text = stringResource(R.string.unnecessary),
                    onClick = { onBowelAssistanceChanged(false) },
                    enable = isBowelAssistance == false,
                    modifier = Modifier.width(104.dp),
                )
            }
        }

        LabeledContent(subtitle = stringResource(id = R.string.walking_assistance)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareChipBasic(
                    text = stringResource(R.string.necessary),
                    onClick = {
                        onWalkingAssistanceChanged(true)
                        coroutineScope.launch {
                            scrollState.animateScrollTo(
                                value = with(density) { 130.dp.toPx() }.toInt(),
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            )
                        }
                    },
                    enable = isWalkingAssistance == true,
                    modifier = Modifier.width(104.dp),
                )

                CareChipBasic(
                    text = stringResource(R.string.unnecessary),
                    onClick = {
                        onWalkingAssistanceChanged(false)
                        coroutineScope.launch {
                            scrollState.animateScrollTo(
                                value = with(density) { 120.dp.toPx() }.toInt(),
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            )
                        }
                    },
                    enable = isWalkingAssistance == false,
                    modifier = Modifier.width(104.dp),
                )
            }
        }

        LabeledContent(
            subtitle = buildAnnotatedString {
                append("일상보조 ")
                withStyle(
                    style = SpanStyle(
                        color = CareTheme.colors.gray300,
                        fontSize = CareTheme.typography.caption.fontSize,
                        fontFamily = PretendardMedium,
                    )
                ) {
                    append("(선택)")
                }
            },
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                maxItemsInEachRow = 3,
                modifier = Modifier.fillMaxWidth()
            ) {
                LifeAssistance.entries.forEach { assistance ->
                    if (assistance == LifeAssistance.NONE) return@forEach

                    CareChipBasic(
                        text = assistance.displayName,
                        onClick = { onLifeAssistanceChanged(assistance) },
                        enable = assistance in lifeAssistance,
                        modifier = Modifier.width(104.dp),
                    )
                }
            }
        }

        LabeledContent(
            subtitle = buildAnnotatedString {
                append("이외에 요구사항이 있다면 적어주세요. ")
                withStyle(
                    style = SpanStyle(
                        color = CareTheme.colors.gray300,
                        fontSize = CareTheme.typography.caption.fontSize,
                        fontFamily = PretendardMedium,
                    )
                ) {
                    append("(선택)")
                }
            },
        ) {
            CareTextFieldLong(
                value = extraRequirement,
                hint = stringResource(id = R.string.speciality_hint),
                onValueChanged = onExtraRequirementChanged,
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
                onClick = { setJobPostingStep(JobPostingStep.findStep(CUSTOMER_REQUIREMENT.step - 1)) },
                modifier = Modifier.weight(1f),
            )

            CareButtonMedium(
                text = stringResource(id = R.string.next),
                enable = isMealAssistance != null && isBowelAssistance != null && isWalkingAssistance != null,
                onClick = { setJobPostingStep(JobPostingStep.findStep(CUSTOMER_REQUIREMENT.step + 1)) },
                modifier = Modifier.weight(1f),
            )
        }
    }

    LogJobPostingStep(step = CUSTOMER_REQUIREMENT)
}