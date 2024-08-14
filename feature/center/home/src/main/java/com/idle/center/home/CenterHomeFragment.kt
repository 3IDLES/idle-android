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
import com.idle.binding.DeepLinkDestination
import com.idle.binding.DeepLinkDestination.CenterApplicantInquiry
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonCardMedium
import com.idle.designsystem.compose.component.CareHeadingTopBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareTabBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.UserRole
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
                setRecruitmentPostStatus = ::setRecruitmentPostStatus,
                navigateTo = { baseEvent(NavigateTo(it)) }
            )
        }
    }
}

@Composable
internal fun CenterHomeScreen(
    recruitmentPostStatus: RecruitmentPostStatus,
    setRecruitmentPostStatus: (RecruitmentPostStatus) -> Unit,
    navigateTo: (DeepLinkDestination) -> Unit,
) {
    Scaffold(
        topBar = {
            CareHeadingTopBar(
                title = stringResource(id = R.string.manage_job_posting),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 8.dp),
            )
        },
        containerColor = CareTheme.colors.white000,
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .padding(top = 20.dp, bottom = 36.dp)
                .fillMaxSize()
        ) {
            CareTabBar(
                selectedStatus = recruitmentPostStatus,
                setStatus = setRecruitmentPostStatus,
                displayName = { it.displayName },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
            )

            CareStateAnimator(
                targetState = recruitmentPostStatus,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            ) { status ->
                when (status) {
                    RecruitmentPostStatus.ONGOING ->
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(listOf(1, 2, 3, 4, 5)) {
                                CenterRecruitmentCard(navigateTo)
                            }

                            item {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(20.dp)
                                )
                            }
                        }

                    RecruitmentPostStatus.PREVIOUS ->
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(listOf(1, 2, 3, 4, 5)) {
                                CenterRecruitmentCard(navigateTo)
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
    }
}

@Composable
private fun CenterRecruitmentCard(
    navigateTo: (DeepLinkDestination) -> Unit,
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
        modifier = Modifier.clickable {
            navigateTo(
                DeepLinkDestination.WorkerJobDetail(
                    jobPostingId = "01914eaa-5106-74ab-a079-67875c1d0f42",
                    userRole = UserRole.CENTER,
                )
            )
        }
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

            CareButtonCardMedium(
                text = "지원자 2명 조회",
                onClick = { navigateTo(CenterApplicantInquiry("01914eaa-5106-74ab-a079-67875c1d0f42")) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4  .dp),
            )

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