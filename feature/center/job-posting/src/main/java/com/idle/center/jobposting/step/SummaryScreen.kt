package com.idle.center.jobposting.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.idle.center.jobposting.JobPostingStep
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.LifeAssistance
import com.idle.domain.model.job.MentalStatus
import com.idle.domain.model.job.PayType
import java.time.LocalDate

@Composable
internal fun SummaryScreen(
    weekDays: Set<DayOfWeek>,
    workStartTime: String,
    workEndTime: String,
    payType: PayType?,
    payAmount: String,
    roadNameAddress: String,
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
    speciality: String,
    isExperiencePreferred: Boolean?,
    applyMethod: Set<ApplyMethod>,
    applyDeadline: LocalDate?,
    postJobPosting: () -> Unit,
    setJobPostingStep: (JobPostingStep) -> Unit,
) {
    val scrollState = rememberScrollState()

    val payText = when (payType!!) {
        PayType.HOURLY -> stringResource(id = R.string.hourly_pay_format, payAmount)
        PayType.WEEKLY -> stringResource(id = R.string.weekly_pay_format, payAmount)
        PayType.MONTHLY -> stringResource(id = R.string.monthly_pay_format, payAmount)
        PayType.UNKNOWN -> ""
    }

    BackHandler { setJobPostingStep(JobPostingStep.findStep(JobPostingStep.SUMMARY.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        Text(
            text = stringResource(id = R.string.summary_title),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 8.dp),
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardColors(
                    containerColor = CareTheme.colors.white000,
                    contentColor = CareTheme.colors.white000,
                    disabledContainerColor = CareTheme.colors.white000,
                    disabledContentColor = CareTheme.colors.white000,
                ),
                border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray100),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        if (isExperiencePreferred == false) {
                            CareTag(
                                text = stringResource(id = R.string.experience_preferred),
                                textColor = CareTheme.colors.orange500,
                                backgroundColor = CareTheme.colors.orange100,
                            )
                        } else {
                            CareTag(
                                text = stringResource(id = R.string.beginner_possible),
                                textColor = CareTheme.colors.orange500,
                                backgroundColor = CareTheme.colors.orange100,
                            )
                        }

                        CareTag(
                            text = applyDeadline?.toString() ?: "",
                            textColor = CareTheme.colors.gray300,
                            backgroundColor = CareTheme.colors.gray050,
                        )
                    }

                    Text(
                        text = roadNameAddress,
                        style = CareTheme.typography.subtitle2,
                        color = CareTheme.colors.gray900,
                        overflow = TextOverflow.Clip,
                        maxLines = 1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 2.dp),
                    )

                    Text(
                        text = stringResource(
                            id = R.string.customer_info_format,
                            careLevel,
                            LocalDate.now().year - birthYear.toInt() + 1,
                            gender.displayName
                        ),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                        modifier = Modifier.padding(end = 8.dp, bottom = 4.dp),
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 2.dp)
                    ) {
                        Image(
                            painter = painterResource(com.idle.designresource.R.drawable.ic_clock),
                            contentDescription = null,
                        )

                        Text(
                            text = stringResource(
                                id = R.string.work_days_hours_format,
                                weekDays.sortedBy { it.ordinal }
                                    .joinToString(", ") { it.displayName },
                                workStartTime,
                                workEndTime
                            ),
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray500,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Image(
                            painter = painterResource(com.idle.designresource.R.drawable.ic_money),
                            contentDescription = null,
                        )

                        Text(
                            text = payText,
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray500,
                        )
                    }
                }
            }

            Row {
                Text(
                    text = stringResource(id = R.string.view_as_caregiver),
                    style = CareTheme.typography.body2,
                    color = CareTheme.colors.gray300,
                )

                Image(
                    painter = painterResource(com.idle.designresource.R.drawable.ic_arrow_right),
                    contentDescription = null,
                )
            }
        }

        HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            Text(
                text = stringResource(id = R.string.work_conditions),
                style = CareTheme.typography.subtitle1,
                color = CareTheme.colors.gray900,
                modifier = Modifier.padding(bottom = 12.dp),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(id = R.string.work_days),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = stringResource(id = R.string.work_hours),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = stringResource(id = R.string.pay),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = stringResource(id = R.string.work_address),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = weekDays
                            .sortedBy { it.ordinal }
                            .joinToString(", ") { it.displayName },
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "${workStartTime} - ${workEndTime}",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = payText,
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = roadNameAddress,
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )
                }
            }
        }

        HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.customer_info),
                style = CareTheme.typography.subtitle1,
                color = CareTheme.colors.gray900,
                modifier = Modifier.padding(bottom = 20.dp),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.width(60.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.name),
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray300,
                        )

                        Text(
                            text = "",
                            style = CareTheme.typography.caption,
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.gender),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = stringResource(id = R.string.age),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = stringResource(id = R.string.weight),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = clientName,
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray900,
                        )

                        Text(
                            text = stringResource(id = R.string.customer_name_note),
                            style = CareTheme.typography.caption,
                            color = CareTheme.colors.gray300,
                        )
                    }

                    Text(
                        text = gender.displayName,
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "${birthYear}년생",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "${weight}kg",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )
                }
            }

            HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.width(60.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.care_level),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = stringResource(id = R.string.mental_status),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = stringResource(id = R.string.disease),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${careLevel}등급",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = mentalStatus.displayName,
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = disease.ifBlank { "-" },
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )
                }
            }

            HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.width(60.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.meal_assistance),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = stringResource(id = R.string.bowel_assistance),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = stringResource(id = R.string.walking_assistance),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = stringResource(id = R.string.life_assistance),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isMealAssistance!!) stringResource(id = R.string.necessary) else stringResource(
                            id = R.string.unnecessary
                        ),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = if (isBowelAssistance!!) stringResource(id = R.string.necessary) else stringResource(
                            id = R.string.unnecessary
                        ),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = if (isWalkingAssistance!!) stringResource(id = R.string.necessary) else stringResource(
                            id = R.string.unnecessary
                        ),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = lifeAssistance
                            .sortedBy { it.ordinal }
                            .joinToString(", ") { it.displayName }
                            .ifEmpty { "-" },
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )
                }
            }

            if (speciality.isNotBlank()) {
                HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

                Text(
                    text = stringResource(id = R.string.speciality),
                    style = CareTheme.typography.body2,
                    color = CareTheme.colors.gray300,
                    modifier = Modifier.padding(top = 16.dp, bottom = 6.dp),
                )

                CareTextFieldLong(
                    value = speciality,
                    enabled = false,
                    onValueChanged = {},
                )
            }
        }

        HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 28.dp)
        ) {
            Text(
                text = stringResource(id = R.string.additional_info),
                style = CareTheme.typography.subtitle1,
                color = CareTheme.colors.gray900,
                modifier = Modifier.padding(bottom = 20.dp),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(id = R.string.experience_preference),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = stringResource(id = R.string.apply_method),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = stringResource(id = R.string.apply_deadline),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isExperiencePreferred!!) stringResource(id = R.string.experience_preferred)
                        else stringResource(id = R.string.beginner_possible),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = applyMethod.joinToString(", ") { it.displayName },
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = applyDeadline?.toString() ?: "",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )
                }
            }
        }

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
                text = stringResource(id = R.string.confirm2),
                onClick = postJobPosting,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 28.dp),
            )
        }
    }
}
