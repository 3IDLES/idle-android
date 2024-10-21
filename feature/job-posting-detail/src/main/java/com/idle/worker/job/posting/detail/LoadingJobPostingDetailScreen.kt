package com.idle.worker.job.posting.detail

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
internal fun LoadingJobPostingDetailScreen() {
    val transition = rememberInfiniteTransition()
    val skeletonColor by transition.animateColor(
        initialValue = CareTheme.colors.gray100,
        targetValue = CareTheme.colors.gray100.copy(alpha = 0.5f),
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ),
    )

    val onBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val scrollState = rememberScrollState()

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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 50.dp, bottom = 8.dp)
                            .height(24.dp)
                            .background(skeletonColor, RoundedCornerShape(4.dp)),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 150.dp, bottom = 8.dp)
                            .height(24.dp)
                            .background(skeletonColor, RoundedCornerShape(4.dp)),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 50.dp, bottom = 2.dp)
                            .padding(end = 50.dp, bottom = 2.dp)
                            .height(20.dp)
                            .background(skeletonColor, RoundedCornerShape(4.dp)),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .padding(end = 150.dp)
                            .background(skeletonColor, RoundedCornerShape(4.dp)),
                    )
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

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 40.dp, bottom = 8.dp)
                            .height(20.dp)
                            .background(skeletonColor, RoundedCornerShape(4.dp)),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 150.dp, bottom = 24.dp)
                            .height(24.dp)
                            .background(skeletonColor, RoundedCornerShape(4.dp)),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(214.dp)
                            .background(skeletonColor, RoundedCornerShape(8.dp)),
                    )
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

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = stringResource(id = R.string.work_days),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.work_hours),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.pay),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.work_address),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
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
                        text = stringResource(id = R.string.customer_info),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.black,
                        modifier = Modifier.padding(bottom = 20.dp),
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.width(60.dp),
                        ) {
                            Text(
                                text = stringResource(id = R.string.gender),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.age),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.weight),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )
                        }
                    }

                    HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = stringResource(id = R.string.care_level),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.mental_status),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.disease),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )
                        }
                    }

                    HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.width(60.dp),
                        ) {
                            Text(
                                text = stringResource(id = R.string.meal_assistance),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.bowel_assistance),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.walking_assistance),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.life_assistance),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )
                        }
                    }

                    HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

                    Text(
                        text = stringResource(id = R.string.speciality),
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray300,
                        modifier = Modifier.padding(top = 16.dp, bottom = 6.dp),
                    )

                    Card(
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray100),
                        colors = CardColors(
                            containerColor = CareTheme.colors.white000,
                            contentColor = CareTheme.colors.white000,
                            disabledContentColor = CareTheme.colors.white000,
                            disabledContainerColor = CareTheme.colors.white000,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(156.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, start = 16.dp, end = 84.dp)
                                .height(20.dp)
                                .background(skeletonColor, RoundedCornerShape(4.dp)),
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
                        text = stringResource(id = R.string.additional_info),
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
                                text = stringResource(id = R.string.experience_preference),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.apply_method),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )

                            Text(
                                text = stringResource(id = R.string.apply_deadline),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray300,
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 56.dp)
                                    .background(skeletonColor, RoundedCornerShape(4.dp)),
                            )
                        }
                    }
                }

                HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 48.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.center_info),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.black,
                        modifier = Modifier.padding(bottom = 20.dp),
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(82.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(20.dp)
                                        .padding(end = 90.dp)
                                        .background(skeletonColor, RoundedCornerShape(4.dp)),
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(16.dp)
                                        .padding(end = 165.dp)
                                        .background(skeletonColor, RoundedCornerShape(4.dp)),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    TrackScreenViewEvent(screenName = "caremeet_job_posting_detail_loading_screen")
}