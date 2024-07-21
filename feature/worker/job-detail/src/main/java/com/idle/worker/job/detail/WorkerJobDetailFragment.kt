package com.idle.worker.job.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareSubtitleTopAppBar
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerJobDetailFragment : BaseComposeFragment() {
    override val fragmentViewModel: WorkerJobDetailViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            WorkerJobDetailScreen()
        }
    }
}

@Composable
internal fun WorkerJobDetailScreen(
) {
    Scaffold(
        containerColor = CareTheme.colors.white000,
        topBar = {
            CareSubtitleTopAppBar(
                title = "공고 정보",
                modifier = Modifier.padding(start = 12.dp, top = 48.dp, bottom = 24.dp),
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValue)
                .background(CareTheme.colors.red)
        ) {

        }
    }
}