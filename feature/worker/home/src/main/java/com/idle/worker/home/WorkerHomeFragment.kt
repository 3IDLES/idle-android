package com.idle.worker.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import com.idle.compose.base.BaseComposeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerHomeFragment : BaseComposeFragment() {
    override val fragmentViewModel: WorkerHomeViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            WorkerHomeScreen()
        }
    }
}

@Composable
internal fun WorkerHomeScreen(
) {
    Scaffold { paddingValue ->
        Box(
            modifier = Modifier.padding(paddingValue)
                .fillMaxSize()
        ) {
            Text(
                text = "워커 홈...!",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}