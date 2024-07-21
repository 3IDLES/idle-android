package com.idle.worker.job.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareButtonStrokeSmall
import com.idle.designsystem.compose.component.CareSubtitleTopAppBar
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerJobDetailFragment : BaseComposeFragment() {
    override val fragmentViewModel: WorkerJobDetailViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            WorkerJobDetailScreen()
        }
    }
}

@Composable
internal fun WorkerJobDetailScreen(
) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = CareTheme.colors.white000,
        topBar = {
            CareSubtitleTopAppBar(
                title = "공고 정보",
                modifier = Modifier.padding(start = 12.dp, top = 48.dp, bottom = 24.dp),
            )
        }
    ) { paddingValue ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
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

                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(com.idle.designresource.R.drawable.ic_star_gray),
                        contentDescription = null,
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
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 8.dp),
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

            HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "근무 장소",
                    style = CareTheme.typography.subtitle1,
                    color = CareTheme.colors.gray900,
                    modifier = Modifier.padding(bottom = 12.dp),
                )

                Text(
                    text = "거주지에서 서울특별시 중 순화동 151까지",
                    style = CareTheme.typography.body2,
                    color = CareTheme.colors.gray500,
                    modifier = Modifier.padding(bottom = 4.dp),
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 16.dp),
                ) {
                    Image(
                        painter = painterResource(com.idle.designresource.R.drawable.ic_walk),
                        contentDescription = null,
                    )

                    Text(
                        text = "걸어서 5분 ~ 10 소요",
                        style = CareTheme.typography.subtitle2,
                        color = CareTheme.colors.gray500,
                    )
                }

                Image(
                    painter = painterResource(com.idle.designresource.R.drawable.ic_temp_map),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "근무 조건",
                    style = CareTheme.typography.subtitle1,
                    color = CareTheme.colors.gray900,
                    modifier = Modifier.padding(bottom = 20.dp),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 8.dp),
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
                            text = "월, 화, 수, 목, 금",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray900,
                        )

                        Text(
                            text = "09:00 - 15:00",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray900,
                        )

                        Text(
                            text = "12,500원",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray900,
                        )

                        Text(
                            text = "서울특별시 중구 순화동 151",
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
                    .padding(horizontal = 20.dp)
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

            HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "기관 정보",
                    style = CareTheme.typography.subtitle1,
                    color = CareTheme.colors.gray900,
                    modifier = Modifier.padding(bottom = 12.dp),
                )

                Card(
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray100),
                    colors = CardColors(
                        containerColor = CareTheme.colors.white000,
                        contentColor = CareTheme.colors.white000,
                        disabledContentColor = CareTheme.colors.white000,
                        disabledContainerColor = CareTheme.colors.white000,
                    ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "네얼간이 요양보호소",
                                style = CareTheme.typography.subtitle3,
                                color = CareTheme.colors.gray900,
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Image(
                                    painter = painterResource(com.idle.designresource.R.drawable.ic_address_pin),
                                    contentDescription = null,
                                )

                                Text(
                                    text = "용인시 어쩌고 저쩌고",
                                    style = CareTheme.typography.body3,
                                    color = CareTheme.colors.gray500,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Image(
                            painter = painterResource(com.idle.designresource.R.drawable.ic_arrow_right),
                            contentDescription = null,
                        )
                    }
                }
            }

            Row(modifier = Modifier.padding(top = 16.dp, bottom = 30.dp)){
                CareButtonStrokeSmall()
            }
        }
    }
}