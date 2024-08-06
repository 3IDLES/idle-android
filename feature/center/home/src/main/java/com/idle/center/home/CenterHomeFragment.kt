package com.idle.center.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonCardMedium
import com.idle.designsystem.compose.component.CareHeadingTopAppBar
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterHomeFragment : BaseComposeFragment() {
    override val fragmentViewModel: CenterHomeViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val recruitmentPostStatus by recruitmentPostStatus.collectAsStateWithLifecycle()
            CenterHomeScreen(
                recruitmentPostStatus = recruitmentPostStatus,
                setRecruitmentPostStatus = ::setRecruitmentPostStatus
            )
        }
    }
}

@Composable
internal fun CenterHomeScreen(
    recruitmentPostStatus: RecruitmentPostStatus,
    setRecruitmentPostStatus: (RecruitmentPostStatus) -> Unit,
) {
    Scaffold(
        topBar = {
            CareHeadingTopAppBar(
                title = stringResource(id = R.string.manage_job_posting),
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 50.dp,
                    bottom = 30.dp
                ),
            )
        },
        containerColor = CareTheme.colors.white000,
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxSize()
        ) {
            Row(modifier = Modifier.padding(bottom = 20.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { setRecruitmentPostStatus(RecruitmentPostStatus.ONGOING) },
                ) {
                    Text(
                        text = stringResource(id = R.string.ongoing_job_posting),
                        style = CareTheme.typography.subtitle3,
                        color = if (recruitmentPostStatus == RecruitmentPostStatus.ONGOING) CareTheme.colors.gray900
                        else CareTheme.colors.gray300,
                        modifier = Modifier.padding(vertical = 12.dp),
                    )

                    if (recruitmentPostStatus == RecruitmentPostStatus.ONGOING) {
                        HorizontalDivider(thickness = 2.dp, color = CareTheme.colors.gray900)
                    } else {
                        HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { setRecruitmentPostStatus(RecruitmentPostStatus.PREVIOUS) },
                ) {
                    Text(
                        text = stringResource(id = R.string.previous_job_posting),
                        style = CareTheme.typography.subtitle3,
                        color = if (recruitmentPostStatus == RecruitmentPostStatus.PREVIOUS) CareTheme.colors.gray900
                        else CareTheme.colors.gray300,
                        modifier = Modifier.padding(vertical = 12.dp),
                    )

                    if (recruitmentPostStatus == RecruitmentPostStatus.PREVIOUS) {
                        HorizontalDivider(thickness = 2.dp, color = CareTheme.colors.gray900)
                    } else {
                        HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)
                    }
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            ) {
                items(listOf(1, 2, 3, 4, 5)) {
                    CenterRecruitmentCardCard()
                }

                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CenterRecruitmentCardCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardColors(
            containerColor = CareTheme.colors.white000,
            contentColor = CareTheme.colors.white000,
            disabledContainerColor = CareTheme.colors.white000,
            disabledContentColor = CareTheme.colors.white000,
        ),
        border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray100),
        modifier = Modifier.clickable { }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "2024. 07. 10 ~ 2024. 07. 31",
                style = CareTheme.typography.body3,
                color = CareTheme.colors.gray300,
                overflow = TextOverflow.Clip,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            Text(
                text = "서울특별시 강남구 신사동",
                style = CareTheme.typography.subtitle2,
                color = CareTheme.colors.gray900,
                overflow = TextOverflow.Clip,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 2.dp),
            )

            Text(
                text = "홍길동 | 1등급 78세 여성",
                style = CareTheme.typography.body2,
                color = CareTheme.colors.gray500,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                CareButtonCardMedium(
                    text = "지원자 2명 조회",
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )

                CareButtonCardMedium(
                    text = "요양 보호사 찾기",
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_edit_pencil_non_background),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 2.dp),
                )

                Text(
                    text = stringResource(id = R.string.edit_job_posting),
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray500,
                    modifier = Modifier.padding(end = 4.dp),
                )

                Image(
                    painter = painterResource(R.drawable.ic_check_gray),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 2.dp),
                )

                Text(
                    text = stringResource(id = R.string.end_recruiting),
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray500,
                )
            }
        }
    }
}