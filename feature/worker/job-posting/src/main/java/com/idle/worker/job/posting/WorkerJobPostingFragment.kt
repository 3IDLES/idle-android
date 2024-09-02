package com.idle.worker.job.posting

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.binding.DeepLinkDestination
import com.idle.binding.DeepLinkDestination.WorkerJobDetail
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonCardLarge
import com.idle.designsystem.compose.component.CareHeadingTopBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareTabBar
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.jobposting.CrawlingJobPosting
import com.idle.domain.model.jobposting.JobPosting
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.jobposting.WorkerJobPosting
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
internal class WorkerJobPostingFragment : BaseComposeFragment() {
    override val fragmentViewModel: WorkerJobPostingViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val recruitmentPostStatus by recruitmentPostStatus.collectAsStateWithLifecycle()
            val appliedJobPostings by appliedJobPostings.collectAsStateWithLifecycle()
            val favoritesJobPostings by favoritesJobPostings.collectAsStateWithLifecycle()

            LaunchedEffect(true) {
                clearJobPostingStatus()
                getMyFavoritesJobPostings()
                launch { getAppliedJobPostings() }
            }

            WorkerJobPostingScreen(
                recruitmentPostStatus = recruitmentPostStatus,
                appliedJobPostings = appliedJobPostings,
                favoritesJobPostings = favoritesJobPostings,
                setRecruitmentPostStatus = ::setRecruitmentPostStatus,
                applyJobPosting = ::applyJobPosting,
                addFavoriteJobPosting = ::addFavoriteJobPosting,
                removeFavoriteJobPosting = ::removeFavoriteJobPosting,
                navigateTo = { baseEvent(NavigateTo(it)) },
            )
        }
    }
}

@Composable
internal fun WorkerJobPostingScreen(
    recruitmentPostStatus: RecruitmentPostStatus,
    appliedJobPostings: List<JobPosting>,
    favoritesJobPostings: List<JobPosting>,
    setRecruitmentPostStatus: (RecruitmentPostStatus) -> Unit,
    applyJobPosting: (String) -> Unit,
    addFavoriteJobPosting: (String, JobPostingType) -> Unit,
    removeFavoriteJobPosting: (String, JobPostingType) -> Unit,
    navigateTo: (DeepLinkDestination) -> Unit,
) {
    Scaffold(
        containerColor = CareTheme.colors.white000,
        topBar = {
            CareHeadingTopBar(
                title = stringResource(id = R.string.manage_job_posting),
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 48.dp,
                    bottom = 8.dp
                ),
            )
        },
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .padding(top = 20.dp)
                .fillMaxSize(),
        ) {
            CareTabBar(
                selectedStatus = recruitmentPostStatus,
                setStatus = setRecruitmentPostStatus,
                displayName = { it.displayName },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
            )

            CareStateAnimator(targetState = recruitmentPostStatus) { status ->
                when (status) {
                    RecruitmentPostStatus.APPLY -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, top = 16.dp)
                                .fillMaxSize()
                        ) {
                            items(
                                items = appliedJobPostings,
                                key = { it.id },
                            ) { jobPosting ->
                                when (jobPosting.jobPostingType) {
                                    JobPostingType.CAREMEET -> WorkerRecruitmentCard(
                                        jobPosting = jobPosting as WorkerJobPosting,
                                        applyJobPosting = applyJobPosting,
                                        addFavoriteJobPosting = addFavoriteJobPosting,
                                        removeFavoriteJobPosting = removeFavoriteJobPosting,
                                        navigateTo = navigateTo,
                                    )

                                    else -> WorkerWorkNetCard(
                                        jobPosting = jobPosting as CrawlingJobPosting,
                                        addFavoriteJobPosting = addFavoriteJobPosting,
                                        removeFavoriteJobPosting = removeFavoriteJobPosting,
                                        navigateTo = navigateTo,
                                    )
                                }
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

                    RecruitmentPostStatus.MARKED -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, top = 16.dp)
                                .fillMaxSize()
                        ) {
                            items(
                                items = favoritesJobPostings,
                                key = { it.id },
                            ) { jobPosting ->
                                when (jobPosting.jobPostingType) {
                                    JobPostingType.CAREMEET -> WorkerRecruitmentCard(
                                        jobPosting = jobPosting as WorkerJobPosting,
                                        applyJobPosting = applyJobPosting,
                                        addFavoriteJobPosting = addFavoriteJobPosting,
                                        removeFavoriteJobPosting = removeFavoriteJobPosting,
                                        navigateTo = navigateTo,
                                    )

                                    else -> WorkerWorkNetCard(
                                        jobPosting = jobPosting as CrawlingJobPosting,
                                        addFavoriteJobPosting = addFavoriteJobPosting,
                                        removeFavoriteJobPosting = removeFavoriteJobPosting,
                                        navigateTo = navigateTo,
                                    )
                                }
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
        }
    }
}

@Composable
private fun WorkerRecruitmentCard(
    jobPosting: WorkerJobPosting,
    applyJobPosting: (String) -> Unit,
    addFavoriteJobPosting: (String, JobPostingType) -> Unit,
    removeFavoriteJobPosting: (String, JobPostingType) -> Unit,
    navigateTo: (DeepLinkDestination) -> Unit,
) {
    val starTintColor by animateColorAsState(
        targetValue = if (jobPosting.isFavorite) CareTheme.colors.orange300
        else CareTheme.colors.gray200,
        label = "즐겨찾기 별의 색상을 관리하는 애니메이션"
    )

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
                WorkerJobDetail(
                    jobPostingId = jobPosting.id,
                    jobPostingType = jobPosting.jobPostingType.name,
                )
            )
        },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                if (!jobPosting.isExperiencePreferred) {
                    CareTag(
                        text = stringResource(id = R.string.beginner_possible),
                        textColor = CareTheme.colors.orange500,
                        backgroundColor = CareTheme.colors.orange100,
                    )
                }

                if (jobPosting.calculateDeadline() <= 14) {
                    CareTag(
                        text = "D-${jobPosting.calculateDeadline()}",
                        textColor = CareTheme.colors.gray300,
                        backgroundColor = CareTheme.colors.gray050,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(com.idle.designresource.R.drawable.ic_star_gray),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(starTintColor),
                    modifier = Modifier.clickable {
                        if (!jobPosting.isFavorite) {
                            addFavoriteJobPosting(jobPosting.id, jobPosting.jobPostingType)
                        } else {
                            removeFavoriteJobPosting(jobPosting.id, jobPosting.jobPostingType)
                        }
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp),
            ) {
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
                    modifier = Modifier.weight(1f),
                )

                Text(
                    text = "도보 ${jobPosting.getDistanceInMinutes()}",
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray500,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }

            Text(
                text = "${jobPosting.careLevel}등급 ${jobPosting.age}세 ${jobPosting.gender.displayName}",
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
                    text = "${
                        jobPosting.weekdays
                            .sortedBy { it.ordinal }
                            .joinToString(", ") { it.displayName }
                    } | ${jobPosting.startTime} - ${jobPosting.endTime}",
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray500,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_money),
                    contentDescription = null,
                )

                Text(
                    text = "${jobPosting.payType.displayName} ${jobPosting.payAmount} 원",
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray500,
                )
            }

            CareButtonCardLarge(
                text = if (jobPosting.applyTime != null) {
                    "지원완료 ${
                        jobPosting.applyTime!!.format(
                            DateTimeFormatter.ofPattern("yyyy. MM. dd")
                        )
                    }"
                } else stringResource(id = R.string.recruit),
                enable = jobPosting.applyTime == null,
                onClick = { applyJobPosting(jobPosting.id) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun WorkerWorkNetCard(
    jobPosting: CrawlingJobPosting,
    addFavoriteJobPosting: (String, JobPostingType) -> Unit,
    removeFavoriteJobPosting: (String, JobPostingType) -> Unit,
    navigateTo: (DeepLinkDestination) -> Unit,
) {
    val starTintColor by animateColorAsState(
        targetValue = if (jobPosting.isFavorite) CareTheme.colors.orange300
        else CareTheme.colors.gray200,
        label = "즐겨찾기 별의 색상을 관리하는 애니메이션"
    )

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
                WorkerJobDetail(
                    jobPostingId = jobPosting.id,
                    jobPostingType = jobPosting.jobPostingType.name,
                )
            )
        },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                CareTag(
                    text = stringResource(id = R.string.worknet),
                    textColor = Color(0xFF2B8BDC),
                    backgroundColor = Color(0xFFD3EBFF),
                )

                if (jobPosting.calculateDeadline() <= 14) {
                    CareTag(
                        text = "D-${jobPosting.calculateDeadline()}",
                        textColor = CareTheme.colors.gray300,
                        backgroundColor = CareTheme.colors.gray050,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(R.drawable.ic_star_gray),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(starTintColor),
                    modifier = Modifier.clickable {
                        if (!jobPosting.isFavorite) {
                            addFavoriteJobPosting(jobPosting.id, jobPosting.jobPostingType)
                        } else {
                            removeFavoriteJobPosting(jobPosting.id, jobPosting.jobPostingType)
                        }
                    }
                )
            }

            Text(
                text = jobPosting.title,
                style = CareTheme.typography.subtitle2,
                color = CareTheme.colors.gray900,
                modifier = Modifier.padding(vertical = 8.dp),
            )

            Text(
                text = "도보 ${jobPosting.getDistanceInMinutes()}",
                style = CareTheme.typography.body3,
                color = CareTheme.colors.gray900,
                modifier = Modifier.padding(bottom = 4.dp),
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
                    text = "${jobPosting.workingSchedule} | ${jobPosting.workingTime}",
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray500,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_money),
                    contentDescription = null,
                )

                Text(
                    text = jobPosting.payInfo,
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray500,
                )
            }
        }
    }
}