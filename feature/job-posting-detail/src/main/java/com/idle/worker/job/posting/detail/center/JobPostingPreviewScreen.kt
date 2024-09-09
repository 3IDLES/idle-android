package com.idle.worker.job.posting.detail.center

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLine
import com.idle.designsystem.compose.component.CareCard
import com.idle.designsystem.compose.component.CareMap
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.DayOfWeek
import com.idle.domain.model.jobposting.LifeAssistance
import com.idle.domain.model.jobposting.MentalStatus
import com.idle.domain.model.jobposting.PayType
import com.idle.domain.model.profile.CenterProfile
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun JobPostingPreviewScreen(
    weekdays: Set<DayOfWeek>,
    workStartTime: String,
    workEndTime: String,
    payType: PayType,
    payAmount: String,
    lotNumberAddress: String,
    gender: Gender,
    birthYear: String,
    weight: String,
    careLevel: String,
    mentalStatus: MentalStatus,
    disease: String?,
    isMealAssistance: Boolean,
    isBowelAssistance: Boolean,
    isWalkingAssistance: Boolean,
    lifeAssistance: Set<LifeAssistance>,
    extraRequirement: String,
    isExperiencePreferred: Boolean,
    applyMethod: Set<ApplyMethod>,
    applyDeadline: LocalDate?,
    centerProfile: CenterProfile?,
    onBackPressed: (() -> Unit),
) {
    val scrollState = rememberScrollState()
    val age = (LocalDate.now(ZoneId.of("Asia/Seoul")).year - birthYear.toInt() + 1)

    BackHandler { onBackPressed() }

    Scaffold(
        containerColor = CareTheme.colors.white000,
        topBar = {
            CareSubtitleTopBar(
                title = stringResource(id = R.string.job_posting_detail),
                modifier = Modifier.padding(
                    start = 12.dp,
                    top = 48.dp,
                    end = 20.dp,
                    bottom = 12.dp
                ),
                onNavigationClick = { onBackPressed() },
            )
        },
    ) { paddingValue ->
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .padding(paddingValue)
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        if (!isExperiencePreferred) {
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

                        Spacer(modifier = Modifier.weight(1f))

                        Image(
                            painter = painterResource(R.drawable.ic_star_gray),
                            contentDescription = null,
                        )
                    }

                    Text(
                        text = try {
                            lotNumberAddress.split(" ").subList(0, 3)
                                .joinToString(" ")
                        } catch (e: IndexOutOfBoundsException) {
                            ""
                        },
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.gray900,
                        overflow = TextOverflow.Clip,
                        maxLines = 1,
                        modifier = Modifier.padding(bottom = 2.dp),
                    )

                    Text(
                        text = "${careLevel}등급 ${age}세 ${gender.displayName}",
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
                            painter = painterResource(R.drawable.ic_clock),
                            contentDescription = null,
                        )

                        Text(
                            text = "${
                                weekdays.toList().sortedBy { it.ordinal }
                                    .joinToString(",") { it.displayName }
                            } | $workStartTime - $workEndTime",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray500,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_money),
                            contentDescription = null,
                        )

                        Text(
                            text = "${payType.displayName} ${payAmount}원",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray500,
                        )
                    }
                }

                HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.work_address),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.gray900,
                        modifier = Modifier.padding(bottom = 20.dp),
                    )

                    Text(
                        text = "거주지에서 $lotNumberAddress 까지",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray500,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_walk),
                            contentDescription = null,
                        )

                        Text(
                            text = "걸어서 5분 ~ 10분 소요",
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.gray900,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(224.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    ) {
                        CareMap(
                            homeLatLng = centerProfile?.let { it.latitude to it.longitude }
                                ?: (37.503561620195 to 127.04492840406),
                            workspaceLatLng = (37.503561620195 to 127.04492840406),
                            onMapClick = { },
                            modifier = Modifier.fillMaxSize(),
                        )

                        Image(
                            painter = painterResource(id = R.drawable.ic_map_expand),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp),
                        )

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xff5E6573).copy(alpha = 0.7f))
                                .align(Alignment.BottomCenter),
                        ) {
                            Text(
                                text = "이미지 속 위치는 실제 위치가 아니며, 실제 공고 등록 시 \n" +
                                        "근무지 위치와 요양보호사의 위치가 반영되어 표시됩니다.",
                                style = CareTheme.typography.caption,
                                color = CareTheme.colors.white000,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
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
                        text = stringResource(id = R.string.work_conditions),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.gray900,
                        modifier = Modifier.padding(bottom = 20.dp),
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
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
                                text = weekdays.toList()
                                    .sortedBy { it.ordinal }
                                    .joinToString(",") { it.displayName },
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "$workStartTime - $workEndTime",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "${payType.displayName} $payAmount 원",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = lotNumberAddress,
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
                            Text(
                                text = gender.displayName,
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "${age}세",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = if (weight.isNotBlank()) "${weight}kg" else "-",
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
                                text = if (disease.isNullOrBlank()) "-"
                                else disease,
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

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = if (isMealAssistance) stringResource(
                                    id = R.string.necessary
                                )
                                else stringResource(id = R.string.unnecessary),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = if (isBowelAssistance) stringResource(
                                    id = R.string.necessary
                                )
                                else stringResource(id = R.string.unnecessary),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = if (isWalkingAssistance) stringResource(
                                    id = R.string.necessary
                                )
                                else stringResource(id = R.string.unnecessary),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = lifeAssistance.toList()
                                    .sortedBy { it.ordinal }
                                    .joinToString(",") { it.displayName },
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )
                        }
                    }

                    HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

                    Text(
                        text = stringResource(id = R.string.speciality),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                        modifier = Modifier.padding(top = 16.dp, bottom = 6.dp),
                    )

                    CareTextFieldLong(
                        value = extraRequirement.ifBlank { "-" },
                        enabled = false,
                        onValueChanged = {},
                    )
                }

                HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.additional_info),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.gray900,
                        modifier = Modifier.padding(bottom = 20.dp),
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        modifier = Modifier.fillMaxWidth(),
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

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = if (isExperiencePreferred) stringResource(
                                    id = R.string.experience_preferred
                                )
                                else stringResource(id = R.string.beginner_possible),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = applyMethod.toList()
                                    .sortedBy { it.ordinal }
                                    .joinToString(",") { it.displayName },
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = applyDeadline.toString(),
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
                        .padding(start = 20.dp, end = 20.dp, bottom = 48.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.center_info),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.gray900,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )

                    CareCard(
                        title = centerProfile?.centerName ?: "케어밋",
                        description = centerProfile?.roadNameAddress ?: "서울특별시 강남구 테헤란로 311",
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CareTheme.colors.white000)
                    .padding(top = 12.dp, bottom = 28.dp, start = 20.dp, end = 20.dp),
            ) {
                CareButtonLine(
                    text = stringResource(id = R.string.inquiry),
                    onClick = {},
                    borderColor = CareTheme.colors.orange400,
                    textColor = CareTheme.colors.orange500,
                    modifier = Modifier.weight(1f),
                )

                CareButtonLine(
                    text = stringResource(id = R.string.recruit),
                    onClick = {},
                    containerColor = CareTheme.colors.orange500,
                    borderColor = CareTheme.colors.orange500,
                    textColor = CareTheme.colors.white000,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }

    TrackScreenViewEvent(screenName = "job_posting_carer_side_screen")
}