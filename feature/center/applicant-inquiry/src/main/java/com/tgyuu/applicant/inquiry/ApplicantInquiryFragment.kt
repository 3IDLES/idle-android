package com.tgyuu.applicant.inquiry

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.binding.DeepLinkDestination
import com.idle.binding.NavigationEvent
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonCardMedium
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.jobposting.Applicant
import com.idle.domain.model.jobposting.JobPostingSummary
import com.idle.domain.model.profile.JobSearchStatus.NO
import com.idle.domain.model.profile.JobSearchStatus.UNKNOWN
import com.idle.domain.model.profile.JobSearchStatus.YES
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ApplicantInquiryFragment : BaseComposeFragment() {
    private val args: ApplicantInquiryFragmentArgs by navArgs()
    override val fragmentViewModel: ApplicantInquiryViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val jobPostingSummary by jobPostingSummary.collectAsStateWithLifecycle()
            val applicants by applicants.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                getApplicantsInfo(args.jobPostingId)
            }

            jobPostingSummary?.let {
                ApplicantInquiryScreen(
                    jobPostingSummary = jobPostingSummary!!,
                    applicants = applicants,
                    navigateTo = { navigationHelper.navigateTo(NavigationEvent.NavigateTo(it)) },
                )
            }
        }
    }
}

@Composable
internal fun ApplicantInquiryScreen(
    jobPostingSummary: JobPostingSummary,
    applicants: List<Applicant>,
    navigateTo: (DeepLinkDestination) -> Unit
) {
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
            RecruitInfoCard(jobPostingSummary)

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
                        color = CareTheme.colors.black,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }

                items(
                    items = applicants,
                    key = { it.carerId },
                ) { applicant ->
                    WorkerProfileCard(
                        applicant = applicant,
                        navigateTo = navigateTo,
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun RecruitInfoCard(jobPostingSummary: JobPostingSummary) {
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
                text = "${jobPostingSummary.createdAt} ~ ${jobPostingSummary.applyDeadline}",
                style = CareTheme.typography.body3,
                color = CareTheme.colors.gray300,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            Text(
                text = try {
                    jobPostingSummary.lotNumberAddress.split(" ").subList(0, 3).joinToString(" ")
                } catch (e: IndexOutOfBoundsException) {
                    ""
                },
                style = CareTheme.typography.subtitle2,
                color = CareTheme.colors.black,
                modifier = Modifier.padding(bottom = 2.dp),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(IntrinsicSize.Min),
            ) {
                Text(
                    text = jobPostingSummary.clientName,
                    style = CareTheme.typography.body2,
                    color = CareTheme.colors.gray500,
                )

                VerticalDivider(
                    thickness = 1.dp, color = CareTheme.colors.gray500,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )

                Text(
                    text = "${jobPostingSummary.careLevel}등급 ${jobPostingSummary.age}세 ${jobPostingSummary.gender.displayName}",
                    style = CareTheme.typography.body2,
                    color = CareTheme.colors.gray500,
                )
            }
        }
    }
}

@Composable
private fun WorkerProfileCard(
    applicant: Applicant,
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
        onClick = { navigateTo(DeepLinkDestination.WorkerProfile(applicant.carerId)) },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(bottom = 12.dp),
            ) {
                AsyncImage(
                    model = applicant.profileImageUrl ?: R.drawable.ic_worker_profile_default,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 4.dp)
                        .clip(CircleShape)
                        .size(72.dp),
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    when (applicant.jobSearchStatus) {
                        YES -> CareTag(
                            text = applicant.jobSearchStatus.displayName,
                            textColor = CareTheme.colors.orange500,
                            backgroundColor = CareTheme.colors.orange100,
                        )

                        NO -> CareTag(
                            text = applicant.jobSearchStatus.displayName,
                            textColor = CareTheme.colors.gray300,
                            backgroundColor = CareTheme.colors.gray050,
                        )

                        UNKNOWN -> CareTag(
                            text = applicant.jobSearchStatus.displayName,
                            textColor = CareTheme.colors.gray300,
                            backgroundColor = CareTheme.colors.gray100,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = applicant.name,
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.black,
                        )

                        Text(
                            text = "요양보호사",
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.black,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    ) {
                        Text(
                            text = "${applicant.age}세 ${applicant.gender.displayName}",
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray500,
                        )

                        VerticalDivider(
                            thickness = 1.dp, color = CareTheme.colors.gray100,
                            modifier = Modifier.padding(horizontal = 8.dp),
                        )

                        if (applicant.experienceYear != null) {
                            Text(
                                text = "${applicant.experienceYear}년차",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray500,
                            )
                        } else {
                            Text(
                                text = "경력 미기입",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray500,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            CareButtonCardMedium(
                text = "프로필 보기",
                onClick = { navigateTo(DeepLinkDestination.WorkerProfile(applicant.carerId)) },
                containerColor = CareTheme.colors.white000,
                textColor = CareTheme.colors.gray300,
                border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray100),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    TrackScreenViewEvent(screenName = "applicant_inquiry_screen")
}