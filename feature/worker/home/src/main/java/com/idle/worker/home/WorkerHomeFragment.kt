package com.idle.worker.home

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.idle.binding.DeepLinkDestination.WorkerJobDetail
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonCardLarge
import com.idle.designsystem.compose.component.CareHeadingTopBar
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.jobposting.WorkerJobPosting
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerHomeFragment : BaseComposeFragment() {
    override val fragmentViewModel: WorkerHomeViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val jobPostings by jobPostings.collectAsStateWithLifecycle()

            WorkerHomeScreen(
                workerJobPostings = jobPostings,
                getJobPostings = ::getJobPostings,
                navigateTo = { baseEvent(NavigateTo(it)) },
            )
        }
    }
}

@Composable
internal fun WorkerHomeScreen(
    workerJobPostings: List<WorkerJobPosting>,
    getJobPostings: () -> Unit,
    navigateTo: (DeepLinkDestination) -> Unit,
) {
    val listState = rememberLazyListState()

    val isLastElement by remember {
        derivedStateOf {
            val lastVisibleIndex =
                listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size
            workerJobPostings.isNotEmpty() && lastVisibleIndex >= workerJobPostings.size
        }
    }

    LaunchedEffect(isLastElement) {
        if (isLastElement) {
            getJobPostings()
        }
    }

    Scaffold(
        containerColor = CareTheme.colors.white000,
        topBar = {
            CareHeadingTopBar(
                title = "경기 가평군 가평읍",
                leftComponent = {
                    Image(
                        painter = painterResource(R.drawable.ic_address_pin_big),
                        contentDescription = null,
                    )
                },
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 48.dp,
                    bottom = 8.dp
                ),
            )
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .padding(top = 20.dp)
                .fillMaxSize(),
        ) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxSize()
            ) {
                items(
                    items = workerJobPostings,
                    key = { it.id },
                ) { jobPosting ->
                    WorkerRecruitmentCard(
                        workerJobPosting = jobPosting,
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

@Composable
private fun WorkerRecruitmentCard(
    workerJobPosting: WorkerJobPosting,
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
            navigateTo(WorkerJobDetail(jobPostingId = workerJobPosting.id))
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
                if (!workerJobPosting.isExperiencePreferred) {
                    CareTag(
                        text = "초보가능",
                        textColor = CareTheme.colors.orange500,
                        backgroundColor = CareTheme.colors.orange100,
                    )
                }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp),
            ) {
                Text(
                    text = try {
                        workerJobPosting.lotNumberAddress.split(" ").subList(0, 3).joinToString(" ")
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
                    text = "${workerJobPosting.distance} km",
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray500,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }

            Text(
                text = "${workerJobPosting.careLevel}등급 ${workerJobPosting.age}세 ${workerJobPosting.gender.displayName}",
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
                        workerJobPosting.weekdays
                            .sortedBy { it.ordinal }
                            .joinToString(", ") { it.displayName }
                    } | ${workerJobPosting.startTime} - ${workerJobPosting.endTime}",
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
                    text = "${workerJobPosting.payType.displayName} ${workerJobPosting.payAmount} 원",
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray500,
                )
            }

            CareButtonCardLarge(
                text = stringResource(id = R.string.recruit),
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}