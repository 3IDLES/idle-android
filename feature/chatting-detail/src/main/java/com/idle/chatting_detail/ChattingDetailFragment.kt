package com.idle.chatting_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.chatting.detail.R
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareHeadingTopBar
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ChattingDetailFragment : BaseComposeFragment() {
    override val fragmentViewModel: ChattingDetailViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {

            ChattingDetailScreen(
                snackbarHostState = snackbarHostState,
                navigateTo = { baseEvent(NavigateTo(it, popUpTo = R.id.nav_chatting_detail)) },
            )
        }
    }
}

@Composable
internal fun ChattingDetailScreen(
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
        )
    }
}