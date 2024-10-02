package com.idle.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.notification.Notification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class NotificationFragment : BaseComposeFragment() {
    override val fragmentViewModel: NotificationViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val todayNotification by todayNotification.collectAsStateWithLifecycle()
            val weeklyNotification by weeklyNotification.collectAsStateWithLifecycle()
            val monthlyNotification by monthlyNotification.collectAsStateWithLifecycle()

            LaunchedEffect(true) {
                clearNotifications()
                getMyNotifications()
            }

            NotificationScreen(
                snackbarHostState = snackbarHostState,
                todayNotification = todayNotification,
                weeklyNotification = weeklyNotification,
                monthlyNotification = monthlyNotification,
                navigateUp = { findNavController().navigateUp() },
            )
        }
    }
}

@Composable
private fun NotificationScreen(
    snackbarHostState: SnackbarHostState,
    todayNotification: List<Notification>,
    weeklyNotification: List<Notification>,
    monthlyNotification: List<Notification>,
    navigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            CareSubtitleTopBar(
                title = stringResource(id = R.string.notification),
                onNavigationClick = navigateUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, end = 20.dp, bottom = 12.dp),
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    CareSnackBar(
                        data = data,
                        modifier = Modifier.padding(bottom = 138.dp)
                    )
                }
            )
        },
    ) { paddingValue ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterVertically
            ),
            modifier = Modifier
                .fillMaxSize()
                .background(CareTheme.colors.white000)
                .padding(paddingValue),
        ) {
            if (todayNotification.isEmpty() && weeklyNotification.isEmpty() && monthlyNotification.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_received_notification),
                    style = CareTheme.typography.heading2,
                    color = CareTheme.colors.gray900,
                )

                Text(
                    text = stringResource(id = R.string.notification_description),
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray300,
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Text(
                            text = stringResource(id = R.string.today),
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.gray900,
                            modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 8.dp),
                        )
                    }

                    items(items = todayNotification) { notification ->
                        NotificationItem()
                    }

                    item {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .background(CareTheme.colors.gray050)
                        )

                        Text(
                            text = stringResource(id = R.string.recent_a_week),
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.gray900,
                            modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 8.dp),
                        )
                    }

                    items(items = weeklyNotification) { notification ->
                        NotificationItem()
                    }

                    item {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CareTheme.colors.gray050)
                                .height(8.dp)
                        )

                        Text(
                            text = stringResource(id = R.string.other_days),
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.gray900,
                            modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 8.dp),
                        )
                    }

                    items(items = monthlyNotification) { notification ->
                        NotificationItem()
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(CareTheme.colors.gray100)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "30분 전",
                    style = CareTheme.typography.caption,
                    color = CareTheme.colors.gray500,
                    modifier = Modifier.padding(bottom = 2.dp),
                )

                Text(
                    text = "김철수 님이 공고에 지원하였습니다.",
                    style = CareTheme.typography.subtitle3,
                    color = CareTheme.colors.gray900,
                    modifier = Modifier.padding(bottom = 1.dp),
                )

                Text(
                    text = "서울특별시 강남구 신사동 1등급 78세 여성",
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray300,
                )
            }
        }
    }
}