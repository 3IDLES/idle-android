package com.idle.center.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.idle.designsystem.compose.component.CareFloatingButton
import com.idle.designsystem.compose.component.CareHeadingTopBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareTabBar
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
    val onGoingListState = rememberLazyListState()
    val previousListState = rememberLazyListState()
    val isScroll by remember { derivedStateOf { onGoingListState.isScrollInProgress || previousListState.isScrollInProgress } }

    Scaffold(
        topBar = {
            CareHeadingTopBar(
                title = stringResource(id = R.string.manage_job_posting),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 8.dp),
            )
        },
        containerColor = CareTheme.colors.white000,
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValue)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
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
                            LazyColumn(
                                state = onGoingListState,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(
                                    listOf(
                                        mapOf(
                                            "duration" to "2024. 08. 10 ~ 2024. 08. 31",
                                            "lotNumberAddress" to "서울특별시 서초구 반포동",
                                            "subText" to "김민수 | 2등급 85세 남성",
                                            "buttonText" to "지원자 3명 조회"
                                        ),
                                        mapOf(
                                            "duration" to "2024. 08. 16 ~ 2024. 08. 24",
                                            "lotNumberAddress" to "서울특별시 용산구 이태원동",
                                            "subText" to "박영희 | 3등급 72세 여성",
                                            "buttonText" to "지원자 1명 조회"
                                        ),
                                        mapOf(
                                            "duration" to "2024. 08. 13 ~ 2024. 08. 21",
                                            "lotNumberAddress" to "서울특별시 강서구 화곡동",
                                            "subText" to "최준호 | 4등급 79세 남성",
                                            "buttonText" to "지원자 4명 조회"
                                        ),
                                        mapOf(
                                            "duration" to "2024. 08. 17 ~ 2024. 08. 28",
                                            "lotNumberAddress" to "서울특별시 송파구 잠실동",
                                            "subText" to "이지연 | 1등급 90세 여성",
                                            "buttonText" to "지원자 2명 조회"
                                        ),
                                        mapOf(
                                            "duration" to "2024. 07. 17 ~ 2024. 08. 28",
                                            "lotNumberAddress" to "서울특별시 노원구 상계동",
                                            "subText" to "홍길동 | 2등급 82세 남성",
                                            "buttonText" to "지원자 5명 조회"
                                        )
                                    )
                                ) {
                                    CenterRecruitmentCard(
                                        duration = it.get("duration")!!,
                                        lotNumberAddress = it.get("lotNumberAddress")!!,
                                        subText = it.get("subText")!!,
                                        buttonText = it.get("buttonText")!!,
                                        navigateTo = navigateTo,
                                    )
                                }

                                item {
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(28.dp)
                                    )
                                }
                            }

                        RecruitmentPostStatus.PREVIOUS ->
                            LazyColumn(
                                state = previousListState,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(
                                    listOf(
                                        mapOf(
                                            "duration" to "2024. 08. 10 ~ 2024. 08. 31",
                                            "lotNumberAddress" to "서울특별시 서초구 반포동",
                                            "subText" to "김민수 | 2등급 85세 남성",
                                            "buttonText" to "지원자 3명 조회"
                                        ),
                                        mapOf(
                                            "duration" to "2024. 08. 16 ~ 2024. 08. 24",
                                            "lotNumberAddress" to "서울특별시 용산구 이태원동",
                                            "subText" to "박영희 | 3등급 72세 여성",
                                            "buttonText" to "지원자 1명 조회"
                                        ),
                                        mapOf(
                                            "duration" to "2024. 08. 13 ~ 2024. 08. 21",
                                            "lotNumberAddress" to "서울특별시 강서구 화곡동",
                                            "subText" to "최준호 | 4등급 79세 남성",
                                            "buttonText" to "지원자 4명 조회"
                                        ),
                                        mapOf(
                                            "duration" to "2024. 08. 17 ~ 2024. 08. 28",
                                            "lotNumberAddress" to "서울특별시 송파구 잠실동",
                                            "subText" to "이지연 | 1등급 90세 여성",
                                            "buttonText" to "지원자 2명 조회"
                                        ),
                                        mapOf(
                                            "duration" to "2024. 07. 17 ~ 2024. 08. 28",
                                            "lotNumberAddress" to "서울특별시 노원구 상계동",
                                            "subText" to "홍길동 | 2등급 82세 남성",
                                            "buttonText" to "지원자 5명 조회"
                                        )
                                    )
                                ) {
                                    CenterRecruitmentCard(
                                        duration = it.get("duration")!!,
                                        lotNumberAddress = it.get("lotNumberAddress")!!,
                                        subText = it.get("subText")!!,
                                        buttonText = it.get("buttonText")!!,
                                        navigateTo = navigateTo,
                                    )
                                }

                                item {
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(28.dp)
                                    )
                                }
                            }
                    }
                }
            }

            AnimatedVisibility(
                visible = !isScroll,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            ) {
                CareFloatingButton(
                    text = "+ 공고 등록",
                    onClick = { navigateTo(DeepLinkDestination.CenterJobPostingPost) },
                )
            }
        }
    }
}

@Composable
private fun CenterRecruitmentCard(
    duration: String,
    lotNumberAddress: String,
    subText: String,
    buttonText: String,
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
                DeepLinkDestination.CenterJobDetail("01915a82-2910-7ce5-91da-b35d88d30743")
            )
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = duration,
                style = CareTheme.typography.body3,
                color = CareTheme.colors.gray300,
                overflow = TextOverflow.Clip,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            Text(
                text = lotNumberAddress,
                style = CareTheme.typography.subtitle2,
                color = CareTheme.colors.gray900,
                overflow = TextOverflow.Clip,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 2.dp),
            )

            Text(
                text = subText,
                style = CareTheme.typography.body2,
                color = CareTheme.colors.gray500,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            CareButtonCardMedium(
                text = buttonText,
                onClick = { navigateTo(CenterApplicantInquiry("01914eaa-5106-74ab-a079-67875c1d0f42")) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
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