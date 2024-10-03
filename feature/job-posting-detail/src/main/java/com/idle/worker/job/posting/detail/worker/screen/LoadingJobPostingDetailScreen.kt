package com.idle.worker.job.posting.detail.worker.screen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLine
import com.idle.designsystem.compose.component.CareButtonMedium
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
internal fun LoadingJobPostingDetailScreen(snackbarHostState: SnackbarHostState) {
    val transition = rememberInfiniteTransition()

    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 400f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 1000)),
    )

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.9f),
        Color.LightGray.copy(alpha = 0.4f),
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnimation, translateAnimation),
        end = Offset(translateAnimation + 100f, translateAnimation + 100f),
        tileMode = TileMode.Mirror,
    )

    val onBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

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
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    CareSnackBar(
                        data = data,
                        modifier = Modifier.padding(bottom = 116.dp)
                    )
                }
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
                            .height(22.dp)
                            .padding(bottom = 8.dp)
                            .background(brush, RoundedCornerShape(4.dp)),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .padding(end = 100.dp, bottom = 2.dp)
                            .background(brush, RoundedCornerShape(4.dp)),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(29.dp)
                            .padding(end = 150.dp, bottom = 4.dp)
                            .background(brush, RoundedCornerShape(4.dp)),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(26.dp)
                            .padding(end = 80.dp, bottom = 2.dp)
                            .background(brush, RoundedCornerShape(4.dp)),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(26.dp)
                            .padding(end = 200.dp)
                            .background(brush, RoundedCornerShape(4.dp)),
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
                        color = CareTheme.colors.gray900,
                        modifier = Modifier.padding(bottom = 20.dp),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(22.dp)
                            .padding(end = 60.dp, bottom = 4.dp)
                            .background(brush, RoundedCornerShape(4.dp)),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .padding(end = 114.dp, bottom = 20.dp)
                            .background(brush, RoundedCornerShape(4.dp)),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(224.dp)
                            .background(brush, RoundedCornerShape(4.dp)),
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
                        color = CareTheme.colors.gray900,
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
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
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
                        color = CareTheme.colors.gray900,
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
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
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
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
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
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
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

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(156.dp)
                            .background(brush, RoundedCornerShape(4.dp)),
                    )
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
                        color = CareTheme.colors.gray900,
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
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(end = 100.dp)
                                    .background(brush, RoundedCornerShape(4.dp)),
                            )
                        }
                    }
                }

                HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 48.dp)
                        .height(82.dp)
                        .background(brush, RoundedCornerShape(4.dp)),
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CareTheme.colors.white000)
                    .padding(top = 12.dp, bottom = 28.dp, start = 20.dp, end = 20.dp),
            ) {
                CareButtonLine(
                    text = stringResource(id = R.string.inquiry),
                    onClick = {},
                    enable = false,
                    borderColor = CareTheme.colors.orange400,
                    textColor = CareTheme.colors.orange500,
                    modifier = Modifier.weight(1f),
                )

                CareButtonMedium(
                    text = stringResource(id = R.string.recruit),
                    onClick = {},
                    enable = false,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }

    TrackScreenViewEvent(screenName = "carer_caremeet_job_posting_detail_screen")
}