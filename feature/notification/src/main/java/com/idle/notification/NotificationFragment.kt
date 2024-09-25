package com.idle.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class NotificationFragment : BaseComposeFragment() {
    override val fragmentViewModel: NotificationViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            NotificationScreen(
                snackbarHostState = snackbarHostState,
                navigateUp = { findNavController().navigateUp() },
            )
        }
    }
}

@Composable
private fun NotificationScreen(
    snackbarHostState: SnackbarHostState,
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
            modifier = Modifier
                .fillMaxSize()
                .background(CareTheme.colors.white000)
                .padding(paddingValue)
                .padding(start = 20.dp, end = 20.dp, top = 24.dp),
        ) {
        }
    }
}