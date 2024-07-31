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

@Composable
internal fun SummaryScreen(
    setJobPostingStep: (JobPostingStep) -> Unit,
) {
    val scrollState = rememberScrollState()

    BackHandler { setJobPostingStep(JobPostingStep.findStep(JobPostingStep.SUMMARY.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
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
            modifier = Modifier.fillMaxWidth()
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
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        CareTag(
                            text = "초보가능",
                            textColor = CareTheme.colors.orange500,
                            backgroundColor = CareTheme.colors.orange100,
                        )

                        CareTag(
                            text = "D-10",
                            textColor = CareTheme.colors.gray300,
                            backgroundColor = CareTheme.colors.gray050,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 2.dp),
                    ) {
                        Text(
                            text = "서울특별시 강남구 신사동",
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.gray900,
                            overflow = TextOverflow.Clip,
                            maxLines = 1,
                            modifier = Modifier.weight(1f),
                        )

                        Text(
                            text = "도보 15분~20분",
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray500,
                            modifier = Modifier.padding(end = 8.dp),
                        )
                    }

                    Text(
                        text = "1등급 78세 여성",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                        modifier = Modifier.padding(end = 8.dp, bottom = 4.dp),
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 2.dp)
                    ) {
                        Image(
                            painter = painterResource(com.idle.designresource.R.drawable.ic_clock),
                            contentDescription = null,
                        )

                        Text(
                            text = "월, 화, 수, 목, 금 | 09:00 - 15:00",
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
                            text = "시급 12,500 원",
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
            modifier = Modifier.fillMaxWidth()
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
                        text = "근무 요일",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "근무 시간",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "급여",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "근무 주소",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )
                }
            }
        }

        HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

        Column(
            modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 16.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.width(60.dp),
                ) {
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
                    Text(
                        text = "남성",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "74세",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "71kg",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )
                }
            }

            HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.fillMaxWidth()
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
                        text = "2등급",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "치매 초기",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "어쩌고저쩌고",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )
                }
            }

            HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.fillMaxWidth()
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
                        text = "불필요",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "필요",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "필요",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "청소, 말벗",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )
                }
            }

            HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

            Text(
                text = "특이사항",
                style = CareTheme.typography.body2,
                color = CareTheme.colors.gray300,
                modifier = Modifier.padding(top = 16.dp, bottom = 6.dp),
            )

            CareTextFieldLong(
                value = "5~60대 남성 선생님을 선호합니다.",
                enabled = false,
                onValueChanged = {},
            )
        }

        HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

        Column(
            modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier.fillMaxWidth()
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
                        text = "초보 가능",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "전화",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )

                    Text(
                        text = "2024. 11. 12",
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
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
