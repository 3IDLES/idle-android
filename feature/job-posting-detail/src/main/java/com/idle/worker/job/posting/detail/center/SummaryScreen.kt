package com.idle.worker.job.posting.detail.center

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.DayOfWeek
import com.idle.domain.model.jobposting.LifeAssistance
import com.idle.domain.model.jobposting.MentalStatus
import com.idle.domain.model.jobposting.PayType
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun SummaryScreen(
    weekDays: Set<DayOfWeek>,
    workStartTime: String,
    workEndTime: String,
    payType: PayType?,
    payAmount: String,
    roadNameAddress: String,
    lotNumberAddress: String,
    clientName: String,
    gender: Gender,
    age: String,
    weight: String,
    careLevel: String,
    mentalStatus: MentalStatus,
    disease: String,
    isMealAssistance: Boolean?,
    isBowelAssistance: Boolean?,
    isWalkingAssistance: Boolean?,
    lifeAssistance: Set<LifeAssistance>,
    extraRequirement: String?,
    isExperiencePreferred: Boolean?,
    applyMethod: Set<ApplyMethod>,
    applyDeadline: LocalDate?,
    modifier: Modifier = Modifier,
    titleComponent: @Composable (() -> Unit)? = null,
    additionalComponent: @Composable (ColumnScope.() -> Unit)? = null,
    bottomComponent: @Composable (BoxScope.() -> Unit)? = null,
    onClickPreview: () -> Unit,
    onBackPressed: (() -> Unit)? = null,
) {
    val scrollState = rememberScrollState()

    onBackPressed?.let { BackHandler { it.invoke() } }

    val payText = when (payType!!) {
        PayType.HOURLY -> stringResource(id = R.string.hourly_pay_format, payAmount)
        PayType.WEEKLY -> stringResource(id = R.string.weekly_pay_format, payAmount)
        PayType.MONTHLY -> stringResource(id = R.string.monthly_pay_format, payAmount)
        PayType.UNKNOWN -> ""
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            titleComponent?.invoke()

            Column(
                horizontalAlignment = Alignment.Start,
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
                    modifier = Modifier.padding(bottom = 10.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            if (isExperiencePreferred == true) {
                                CareTag(
                                    text = stringResource(id = R.string.beginner_possible),
                                    textColor = CareTheme.colors.orange500,
                                    backgroundColor = CareTheme.colors.orange100,
                                )
                            }

                            CareTag(
                                text = "도보 15분 ~ 20분",
                                textColor = CareTheme.colors.gray300,
                                backgroundColor = CareTheme.colors.gray050,
                            )
                        }

                        Text(
                            text = try {
                                lotNumberAddress.split(" ").subList(0, 3).joinToString(" ")
                            } catch (e: IndexOutOfBoundsException) {
                                ""
                            },
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.black,
                            overflow = TextOverflow.Clip,
                            maxLines = 1,
                            modifier = Modifier.padding(bottom = 2.dp),
                        )

                        Text(
                            text = stringResource(
                                id = R.string.customer_info_format,
                                careLevel,
                                (LocalDate.now(ZoneId.of("Asia/Seoul")).year - age.toInt() + 1),
                                gender.displayName
                            ),
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.view_as_caregiver),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                        modifier = Modifier.clickable { onClickPreview() },
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
                    color = CareTheme.colors.black,
                    modifier = Modifier.padding(bottom = 8.dp),
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
                            color = CareTheme.colors.black,
                        )

                        Text(
                            text = "${workStartTime} - ${workEndTime}",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
                        )

                        Text(
                            text = payText,
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
                        )

                        Text(
                            text = roadNameAddress,
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
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
                    color = CareTheme.colors.black,
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.width(66.dp),
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
                                style = CareTheme.typography.caption1,
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
                                color = CareTheme.colors.black,
                            )

                            Text(
                                text = stringResource(id = R.string.customer_name_note),
                                style = CareTheme.typography.caption1,
                                color = CareTheme.colors.gray300,
                            )
                        }

                        Text(
                            text = gender.displayName,
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
                        )

                        Text(
                            text = "${age}세",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
                        )

                        Text(
                            text = "${weight}kg",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
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
                        modifier = Modifier.width(66.dp)
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
                            color = CareTheme.colors.black,
                        )

                        Text(
                            text = mentalStatus.displayName,
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
                        )

                        Text(
                            text = disease.ifBlank { "-" },
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
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
                        modifier = Modifier.width(66.dp),
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
                            color = CareTheme.colors.black,
                        )

                        Text(
                            text = if (isBowelAssistance!!) stringResource(id = R.string.necessary) else stringResource(
                                id = R.string.unnecessary
                            ),
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
                        )

                        Text(
                            text = if (isWalkingAssistance!!) stringResource(id = R.string.necessary) else stringResource(
                                id = R.string.unnecessary
                            ),
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
                        )

                        Text(
                            text = lifeAssistance
                                .sortedBy { it.ordinal }
                                .joinToString(", ") { it.displayName }
                                .ifEmpty { "-" },
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
                        )
                    }
                }

                if (extraRequirement?.isNotBlank() == true) {
                    HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

                    Text(
                        text = stringResource(id = R.string.speciality),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                        modifier = Modifier.padding(top = 16.dp, bottom = 6.dp),
                    )

                    CareTextFieldLong(
                        value = extraRequirement,
                        enabled = false,
                        onValueChanged = {},
                    )
                }
            }

            HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 24.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.additional_info),
                    style = CareTheme.typography.subtitle1,
                    color = CareTheme.colors.black,
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(28.dp),
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
                            color = CareTheme.colors.black,
                        )

                        Text(
                            text = applyMethod.joinToString(", ") { it.displayName },
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
                        )

                        Text(
                            text = applyDeadline?.toString() ?: "채용시까지",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
                        )
                    }
                }
            }

            additionalComponent?.invoke(this)

            if (bottomComponent != null) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .padding(top = 24.dp),
                )
            }
        }

        bottomComponent?.invoke(this)
    }
}
