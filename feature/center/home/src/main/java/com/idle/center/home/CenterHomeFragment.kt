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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.binding.DeepLinkDestination
import com.idle.binding.DeepLinkDestination.CenterApplicantInquiry
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonCardMedium
import com.idle.designsystem.compose.component.CareDialog
import com.idle.designsystem.compose.component.CareFloatingButton
import com.idle.designsystem.compose.component.CareHeadingTopBar
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareTabBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.jobposting.CenterJobPosting
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class CenterHomeFragment : BaseComposeFragment() {
    override val fragmentViewModel: CenterHomeViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val recruitmentPostStatus by recruitmentPostStatus.collectAsStateWithLifecycle()
            val jobPostingsInProgress by jobPostingsInProgress.collectAsStateWithLifecycle()
            val jobPostingsCompleted by jobPostingsCompleted.collectAsStateWithLifecycle()

            LaunchedEffect(true) {
                clearJobPostingStatus()
                launch { getJobPostingsCompleted() }
                launch { getJobPostingsInProgress() }
                launch { getUnreadNotificationCount() }
            }

            CenterHomeScreen(
                snackbarHostState = snackbarHostState,
                recruitmentPostStatus = recruitmentPostStatus,
                jobPostingsInProgresses = jobPostingsInProgress,
                jobPostingsCompleted = jobPostingsCompleted,
                setRecruitmentPostStatus = ::setRecruitmentPostStatus,
                endJobPosting = ::endJobPosting,
                navigateTo = { baseEvent(NavigateTo(destination = it)) }
            )
        }
    }
}

@Composable
internal fun CenterHomeScreen(
    snackbarHostState: SnackbarHostState,
    recruitmentPostStatus: RecruitmentPostStatus,
    jobPostingsInProgresses: List<CenterJobPosting>,
    jobPostingsCompleted: List<CenterJobPosting>,
    setRecruitmentPostStatus: (RecruitmentPostStatus) -> Unit,
    endJobPosting: (String) -> Unit,
    navigateTo: (DeepLinkDestination) -> Unit,
) {
    val inProgressListState = rememberLazyListState()
    val completedListState = rememberLazyListState()
    val isScroll by remember { derivedStateOf { inProgressListState.isScrollInProgress || completedListState.isScrollInProgress } }
    var showDialog by remember { mutableStateOf(false) }
    var selectedJobPostingId by remember { mutableStateOf("") }

    if (showDialog) {
        CareDialog(
            title = stringResource(id = R.string.end_job_posting_title),
            description = stringResource(id = R.string.end_job_posting_description),
            leftButtonText = stringResource(id = R.string.cancel),
            rightButtonText = stringResource(id = R.string.end),
            leftButtonTextColor = CareTheme.colors.gray300,
            leftButtonColor = CareTheme.colors.white000,
            leftButtonBorder = BorderStroke(1.dp, CareTheme.colors.gray100),
            rightButtonTextColor = CareTheme.colors.white000,
            rightButtonColor = CareTheme.colors.red,
            onDismissRequest = { showDialog = false },
            onLeftButtonClick = { showDialog = false },
            onRightButtonClick = {
                endJobPosting(selectedJobPostingId)
                showDialog = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )
    }

    Scaffold(
        topBar = {
            CareHeadingTopBar(
                title = stringResource(id = R.string.manage_job_posting),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 8.dp),
                rightComponent = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_notification),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            navigateTo(DeepLinkDestination.Notification)
                        }
                    )
                },
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    CareSnackBar(
                        data = data,
                        modifier = Modifier.padding(bottom = 84.dp)
                    )
                }
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
                        RecruitmentPostStatus.IN_PROGRESS -> {
                            LazyColumn(
                                state = inProgressListState,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(
                                    items = jobPostingsInProgresses,
                                    key = { it.id },
                                ) { jobPosting ->
                                    JobPostingInProgressCard(
                                        jobPosting = jobPosting,
                                        navigateTo = navigateTo,
                                        endJobPosting = {
                                            selectedJobPostingId = jobPosting.id
                                            showDialog = true
                                        }
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

                            TrackScreenViewEvent(screenName = "center_home_screen_inprogress")
                        }

                        RecruitmentPostStatus.COMPLETED -> {
                            LazyColumn(
                                state = completedListState,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(
                                    items = jobPostingsCompleted,
                                    key = { it.id },
                                ) { jobPosting ->
                                    JobPostingCompletedCard(
                                        jobPosting = jobPosting,
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

                            TrackScreenViewEvent(screenName = "center_home_screen_completed")
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
private fun JobPostingInProgressCard(
    jobPosting: CenterJobPosting,
    navigateTo: (DeepLinkDestination) -> Unit,
    endJobPosting: () -> Unit,
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
        onClick = { navigateTo(DeepLinkDestination.CenterJobDetail(jobPosting.id)) },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = jobPosting.createdAt + " ~ " + jobPosting.applyDeadline,
                style = CareTheme.typography.body3,
                color = CareTheme.colors.gray300,
                overflow = TextOverflow.Clip,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            Text(
                text = try {
                    jobPosting.lotNumberAddress.split(" ").subList(0, 3).joinToString(" ")
                } catch (e: IndexOutOfBoundsException) {
                    ""
                },
                style = CareTheme.typography.subtitle2,
                color = CareTheme.colors.gray900,
                overflow = TextOverflow.Clip,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 2.dp),
            )

            Text(
                text = "${jobPosting.clientName} | ${jobPosting.careLevel}등급 ${jobPosting.age}세 ${jobPosting.gender.displayName}",
                style = CareTheme.typography.body2,
                color = CareTheme.colors.gray500,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            CareButtonCardMedium(
                text = "지원자 ${jobPosting.applicantCount}명 조회",
                enable = jobPosting.applicantCount > 0,
                onClick = { navigateTo(CenterApplicantInquiry(jobPosting.id)) },
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
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable {
                            navigateTo(
                                DeepLinkDestination.CenterJobDetail(
                                    jobPostingId = jobPosting.id,
                                    isEditState = true,
                                )
                            )
                        },
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
                    modifier = Modifier.clickable { endJobPosting() },
                )
            }
        }
    }
}

@Composable
private fun JobPostingCompletedCard(
    jobPosting: CenterJobPosting,
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
        onClick = { navigateTo(DeepLinkDestination.CenterJobDetail(jobPosting.id)) },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = jobPosting.createdAt + " ~ " + jobPosting.applyDeadline,
                style = CareTheme.typography.body3,
                color = CareTheme.colors.gray300,
                overflow = TextOverflow.Clip,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            Text(
                text = try {
                    jobPosting.lotNumberAddress.split(" ").subList(0, 3).joinToString(" ")
                } catch (e: IndexOutOfBoundsException) {
                    ""
                },
                style = CareTheme.typography.subtitle2,
                color = CareTheme.colors.gray900,
                overflow = TextOverflow.Clip,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 2.dp),
            )

            Text(
                text = "${jobPosting.clientName} | ${jobPosting.careLevel}등급 ${jobPosting.age}세 ${jobPosting.gender.displayName}",
                style = CareTheme.typography.body2,
                color = CareTheme.colors.gray500,
            )
        }
    }
}