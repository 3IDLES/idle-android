package com.tgyuu.applicant.inquiry

import android.util.Log
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonCardMedium
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ApplicantInquiryFragment : BaseComposeFragment() {
    private val args: ApplicantInquiryFragmentArgs by navArgs()
    override val fragmentViewModel: ApplicantInquiryViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {

            LaunchedEffect(Unit) {
                Log.d("test", args.jobPostingId)
            }

            ApplicantInquiryScreen()
        }
    }
}

@Composable
internal fun ApplicantInquiryScreen() {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Scaffold(
        topBar = {
            CareSubtitleTopBar(
                title = stringResource(id = R.string.applicant_inquiry),
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, end = 20.dp, bottom = 12.dp),
            )
        },
        containerColor = CareTheme.colors.white000,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 24.dp)
        ) {
            RecruitInfoCard()

            HorizontalDivider(
                thickness = 8.dp,
                color = CareTheme.colors.gray050,
                modifier = Modifier.padding(vertical = 24.dp),
            )

            LazyColumn(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                modifier = Modifier.padding(horizontal = 20.dp),
            ) {
                item {
                    Text(
                        text = "위 공고에 지원한 보호사 목록이에요.",
                        style = CareTheme.typography.subtitle2,
                        color = CareTheme.colors.gray900,
                        modifier = Modifier.padding(bottom = 20.dp),
                    )
                }

                items(listOf(1, 2, 3)) {
                    WorkerProfileCard()
                }
            }
        }
    }
}

@Composable
private fun RecruitInfoCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardColors(
            containerColor = CareTheme.colors.white000,
            contentColor = CareTheme.colors.white000,
            disabledContainerColor = CareTheme.colors.white000,
            disabledContentColor = CareTheme.colors.white000,
        ),
        border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray100),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "2024. 07. 10 ~ 2024. 07. 31",
                style = CareTheme.typography.body3,
                color = CareTheme.colors.gray300,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            Text(
                text = "서울특별시 강남구 신사동",
                style = CareTheme.typography.subtitle2,
                color = CareTheme.colors.gray900,
                modifier = Modifier.padding(bottom = 2.dp),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(IntrinsicSize.Min),
            ) {
                Text(
                    text = "홍길동",
                    style = CareTheme.typography.body2,
                    color = CareTheme.colors.gray500,
                )

                VerticalDivider(
                    thickness = 1.dp, color = CareTheme.colors.gray500,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )

                Text(
                    text = "1등급 78세 여성",
                    style = CareTheme.typography.body2,
                    color = CareTheme.colors.gray500,
                )
            }
        }
    }
}

@Composable
private fun WorkerProfileCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardColors(
            containerColor = CareTheme.colors.white000,
            contentColor = CareTheme.colors.white000,
            disabledContainerColor = CareTheme.colors.white000,
            disabledContentColor = CareTheme.colors.white000,
        ),
        border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray100),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(bottom = 12.dp),
            ) {
                AsyncImage(
                    model = R.drawable.ic_worker_profile_default,
                    contentDescription = null,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .padding(end = 16.dp),
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    CareTag(
                        text = "구직중",
                        textColor = CareTheme.colors.orange500,
                        backgroundColor = CareTheme.colors.orange100,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "홍길동",
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.gray900,
                        )

                        Text(
                            text = "요양보호사",
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray900,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    ) {
                        Text(
                            text = "58세 여성",
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray500,
                        )

                        VerticalDivider(
                            thickness = 1.dp, color = CareTheme.colors.gray100,
                            modifier = Modifier.padding(horizontal = 8.dp),
                        )

                        Text(
                            text = "1년차",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray500,
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(id = R.drawable.ic_star_gray),
                    contentDescription = null,
                    modifier = Modifier.clickable { },
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareButtonCardMedium(
                    text = "프로필 보기",
                    onClick = { /*TODO*/ },
                    containerColor = CareTheme.colors.white000,
                    textColor = CareTheme.colors.gray300,
                    border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray100),
                    modifier = Modifier.weight(1f),
                )

                CareButtonCardMedium(
                    text = "채용하기",
                    onClick = { /*TODO*/ },
                    containerColor = CareTheme.colors.white000,
                    textColor = CareTheme.colors.orange500,
                    border = BorderStroke(width = 1.dp, color = CareTheme.colors.orange400),
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}