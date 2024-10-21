package com.idle.center.chatting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import coil.compose.AsyncImage
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designsystem.compose.component.CareHeadingTopBar
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterChattingFragment : BaseComposeFragment() {
    override val fragmentViewModel: CenterChattingViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {

            CenterChattingScreen(
                snackbarHostState = snackbarHostState,
                navigateTo = { baseEvent(NavigateTo(it)) },
            )
        }
    }
}

@Composable
internal fun CenterChattingScreen(
    snackbarHostState: SnackbarHostState,
    navigateTo: (DeepLinkDestination) -> Unit,
) {
    Scaffold(
        topBar = {
            CareHeadingTopBar(
                title = stringResource(id = com.idle.designresource.R.string.chatting),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 8.dp),
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
        ) {
            items(items = listOf(1, 2, 3, 4, 5)) {
                ChattingItem(navigateTo = navigateTo)
            }
        }
    }
}

@Composable
internal fun ChattingItem(
    navigateTo: (DeepLinkDestination) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigateTo(
                    DeepLinkDestination.ChattingDetail(
                        chattingRoomId = "",
                        userId = ""
                    )
                )
            },
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            AsyncImage(
                model = com.idle.designresource.R.drawable.ic_notification_placeholder,
                placeholder = painterResource(com.idle.designresource.R.drawable.ic_notification_placeholder),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 12.dp)
                    .size(48.dp),
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 2.dp),
            ) {
                Text(
                    text = "세얼간이요양센터",
                    style = CareTheme.typography.subtitle3,
                    color = CareTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 1.dp),
                )

                Text(
                    text = "안녕하세요 문의드리고 싶어서 연락드렸어요.",
                    style = CareTheme.typography.caption1,
                    color = CareTheme.colors.gray300,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 1.dp),
                )
            }

            Text(
                text = "10월 29일",
                style = CareTheme.typography.caption1,
                color = CareTheme.colors.gray500,
            )
        }
    }
}