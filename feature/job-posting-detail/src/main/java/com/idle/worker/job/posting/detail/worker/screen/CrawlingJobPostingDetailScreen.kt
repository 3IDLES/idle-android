package com.idle.worker.job.posting.detail.worker.screen

import android.content.Intent
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareCard
import com.idle.designsystem.compose.component.CareMap
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.jobposting.CrawlingJobPostingDetail
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.profile.WorkerProfile

@Composable
internal fun CrawlingJobPostingDetailScreen(
    profile: WorkerProfile?,
    jobPostingDetail: CrawlingJobPostingDetail,
    showPlaceDetail: (Boolean) -> Unit,
    addFavoriteJobPosting: (String, JobPostingType) -> Unit,
    removeFavoriteJobPosting: (String, JobPostingType) -> Unit,
) {
    val onBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        containerColor = CareTheme.colors.white000,
        topBar = {
            CareSubtitleTopBar(
                title = stringResource(id = R.string.job_posting_detail),
                modifier = Modifier.padding(
                    start = 12.dp,
                    top = 48.dp,
                    end = 20.dp,
                    bottom = 12.dp
                ),
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
            )
        },
    ) { paddingValue ->
        val starTintColor by animateColorAsState(
            targetValue = if (jobPostingDetail.isFavorite) CareTheme.colors.orange300
            else CareTheme.colors.gray200,
            label = "즐겨찾기 별의 색상을 관리하는 애니메이션"
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .padding(paddingValue)
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
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

                        CareTag(
                            text = "도보 ${jobPostingDetail.getDistanceInMinutes()}",
                            textColor = CareTheme.colors.gray300,
                            backgroundColor = CareTheme.colors.gray050,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Image(
                            painter = painterResource(R.drawable.ic_star_gray),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(starTintColor),
                            modifier = Modifier.clickable {
                                if (!jobPostingDetail.isFavorite) {
                                    addFavoriteJobPosting(
                                        jobPostingDetail.id,
                                        jobPostingDetail.jobPostingType,
                                    )
                                } else {
                                    removeFavoriteJobPosting(
                                        jobPostingDetail.id,
                                        jobPostingDetail.jobPostingType,
                                    )
                                }
                            }
                        )
                    }

                    Text(
                        text = jobPostingDetail.title,
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.black,
                        overflow = TextOverflow.Clip,
                        maxLines = 1,
                        modifier = Modifier.padding(bottom = 2.dp),
                    )

                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 2.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_clock),
                            contentDescription = null,
                        )

                        Text(
                            text = "${jobPostingDetail.workingSchedule}\n${jobPostingDetail.workingTime}",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray500,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_money),
                            contentDescription = null,
                        )

                        Text(
                            text = jobPostingDetail.payInfo,
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray500,
                        )
                    }
                }

                HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.work_address),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.black,
                        modifier = Modifier.padding(bottom = 20.dp),
                    )

                    Text(
                        text = "거주지에서 ${jobPostingDetail.clientAddress} 까지",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray500,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_walk),
                            contentDescription = null,
                        )

                        Text(
                            text = "걸어서 ${jobPostingDetail.getDistanceInMinutes()} 소요",
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.black,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(224.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    ) {
                        if (profile != null) {
                            CareMap(
                                homeLatLng = profile.latitude.toDouble() to profile.longitude.toDouble(),
                                workspaceLatLng = jobPostingDetail.latitude.toDouble() to jobPostingDetail.longitude.toDouble(),
                                onMapClick = { showPlaceDetail(true) },
                                modifier = Modifier.fillMaxSize(),
                            )

                            Image(
                                painter = painterResource(id = R.drawable.ic_map_expand),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(12.dp),
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.ic_map_loading),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                }

                HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.recruitment_guidelines),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.black,
                        modifier = Modifier.padding(bottom = 20.dp),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .border(
                                width = 1.dp,
                                color = CareTheme.colors.gray100,
                                shape = RoundedCornerShape(6.dp),
                            )
                    ) {
                        Text(
                            text = jobPostingDetail.content,
                            style = CareTheme.typography.body3,
                            textAlign = TextAlign.Start,
                            color = CareTheme.colors.black,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        )
                    }
                }

                HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.work_conditions),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.black,
                        modifier = Modifier.padding(bottom = 20.dp),
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = stringResource(id = R.string.work_schedule),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = jobPostingDetail.workingSchedule,
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.black,
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = stringResource(id = R.string.work_hours),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = jobPostingDetail.workingTime,
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.black,
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = stringResource(id = R.string.pay_conditions),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = jobPostingDetail.payInfo,
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.black,
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = stringResource(id = R.string.work_address),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = jobPostingDetail.clientAddress,
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.black,
                            )
                        }
                    }
                }

                HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.admissions_method),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.black,
                        modifier = Modifier.padding(bottom = 20.dp),
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = stringResource(id = R.string.apply_deadline),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.admissions_method),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.apply_method_worknet),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.require_documentation),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = jobPostingDetail.applyDeadline,
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.black,
                            )

                            Text(
                                text = jobPostingDetail.recruitmentProcess,
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.black,
                            )

                            Text(
                                text = jobPostingDetail.applyMethod,
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.black,
                            )

                            Text(
                                text = jobPostingDetail.requireDocumentation,
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.black,
                            )
                        }
                    }
                }

                HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.center_info),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.black,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )

                    CareCard(
                        title = jobPostingDetail.centerName,
                        description = jobPostingDetail.centerAddress,
                        showRightArrow = false,
                        descriptionLeftComponent = {
                            Image(
                                painter = painterResource(R.drawable.ic_address_pin),
                                contentDescription = null,
                            )
                        },
                    )
                }

                HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 38.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.worknet_url),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.black,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )

                    CareCard(
                        title = jobPostingDetail.title,
                        description = jobPostingDetail.jobPostingUrl,
                        showRightArrow = true,
                        onClick = {
                            startActivity(
                                context,
                                Intent(Intent.ACTION_VIEW, jobPostingDetail.jobPostingUrl.toUri()),
                                null
                            )
                        }
                    )
                }
            }
        }
    }

    TrackScreenViewEvent(screenName = "carer_worknet_job_posting_detail_screen")
}