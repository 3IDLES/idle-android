package com.idle.signup.center.step

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.idle.center.jobposting.JobPostingStep
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

@Composable
internal fun SummaryScreen(
    weekDays: Set<DayOfWeek>,
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
    applyDeadline: String,
    postJobPosting: () -> Unit,
    setJobPostingStep: (JobPostingStep) -> Unit,
) {
    val scrollState = rememberScrollState()

    val payText = when (payType!!) {
        PayType.HOURLY -> "시급 $payAmount 원"
        PayType.WEEKLY -> "주급 $payAmount 원"
        PayType.MONTHLY -> "월급 $payAmount 원"
        PayType.UNKNOWN -> ""
    }

    BackHandler { setJobPostingStep(JobPostingStep.findStep(JobPostingStep.SUMMARY.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 20.dp, bottom = 30.dp),
    ) {
        Text(
            text = "다음의 공고 정보가 맞는지 확인해주세요.",
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
                                text = "초보 가능",
                                textColor = CareTheme.colors.orange500,
                                backgroundColor = CareTheme.colors.orange100,
                            )
                        }

                        CareTag(
                            text = applyDeadline,
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
                        text = "1등급 78세 여성",
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
                            text = weekDays
                                .sortedBy { it.ordinal }
                                .joinToString(", ") { it.displayName }
                                    + " | " + "09:00 - 15:00",
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
                    text = "보호사 측 화면으로 보기",
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
                text = "근무 조건",
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
                        text = "근무 요일",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = "근무 시간",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = "급여",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = "근무 주소",
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
                        text = "09:00 - 15:00",
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
                text = "고객 정보",
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
                            text = "이름",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray300,
                        )

                        Text(
                            text = "",
                            style = CareTheme.typography.caption,
                        )
                    }

                    Text(
                        text = "성별",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = "나이",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = "몸무게",
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
                            text = "고객 이름 센터 측에서만 볼 수 있어요.",
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
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "요양등급",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = "인지 상태",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = "질병",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
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
                    modifier = Modifier.width(60.dp),
                ) {
                    Text(
                        text = "식사보조",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = "배변보조",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = "이동보조",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )

                    Text(
                        text = "일상보조",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = if (isMealAssistance!!) "필요" else "불필요",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = if (isBowelAssistance!!) "필요" else "불필요",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = if (isWalkingAssistance!!) "필요" else "불필요",
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
                    text = "특이사항",
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

            HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 28.dp)
            ) {
                Text(
                    text = "추가 지원 정보",
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
                            text = "경력 우대여부",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray300,
                        )

                        Text(
                            text = "지원 방법",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray300,
                        )

                        Text(
                            text = "접수 마감일",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray300,
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = if (isExperiencePreferred!!) "초보 가능" else "경력자 우대",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray900,
                        )

                        Text(
                            text = applyMethod.joinToString(", ") { it.displayName },
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray900,
                        )

                        Text(
                            text = applyDeadline,
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
                    text = "공고 등록 후에도 공고 내용을 수정할 수 있어요.",
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray300,
                )

                CareButtonLarge(
                    text = "확인했어요",
                    onClick = postJobPosting,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}