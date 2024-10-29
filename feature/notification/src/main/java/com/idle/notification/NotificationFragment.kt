package com.idle.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.LoadingCircle
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.notification.Notification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class NotificationFragment : BaseComposeFragment() {
    override val fragmentViewModel: NotificationViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val myNotification by myNotification.collectAsStateWithLifecycle()

            LaunchedEffect(true) {
                getMyNotifications()
            }

            NotificationScreen(
                myNotification = myNotification,
                onNotificationClick = ::onNotificationClick,
                getMyNotifications = ::getMyNotifications,
                navigateUp = { findNavController().navigateUp() },
            )
        }
    }
}

@Composable
private fun NotificationScreen(
    myNotification: List<Notification>?,
    onNotificationClick: (Notification) -> Unit,
    getMyNotifications: () -> Unit,
    navigateUp: () -> Unit,
) {
    val listState = rememberLazyListState()
    val lastVisibleIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size - 1
        }
    }

    val totalItemCount = myNotification?.size ?: 0

    val isNearEnd = if (totalItemCount > 0) {
        lastVisibleIndex >= totalItemCount - 3
    } else {
        false
    }

    LaunchedEffect(isNearEnd) {
        if (totalItemCount != 0 && isNearEnd) {
            getMyNotifications()
        }
    }

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
            if (myNotification?.isEmpty() == true) {
                Text(
                    text = stringResource(id = R.string.no_received_notification),
                    style = CareTheme.typography.heading2,
                    color = CareTheme.colors.black,
                )

                Text(
                    text = stringResource(id = R.string.notification_description),
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray300,
                )
            } else {
                if (myNotification == null) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LoadingCircle(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(bottom = 40.dp),
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = myNotification,
                            key = { it.id },
                        ) { notification ->
                            NotificationItem(
                                notification = notification,
                                onClick = onNotificationClick,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: Notification,
    onClick: (Notification) -> Unit,
    modifier: Modifier = Modifier,
) {
    val notificationItemColor = if (notification.isRead) {
        CareTheme.colors.white000
    } else {
        CareTheme.colors.orange050
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(color = notificationItemColor),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(notification) }
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            AsyncImage(
                model = notification.imageUrl,
                error = painterResource(R.drawable.ic_notification_placeholder),
                placeholder = painterResource(R.drawable.ic_notification_placeholder),
                contentScale = ContentScale.Crop,
                contentDescription = "",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = notification.getNotificationTime(),
                    style = CareTheme.typography.caption1,
                    color = CareTheme.colors.gray500,
                )

                Text(
                    text = notification.title,
                    style = CareTheme.typography.subtitle3,
                    color = CareTheme.colors.black,
                )

                Text(
                    text = notification.body,
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray300,
                )
            }
        }
    }
}