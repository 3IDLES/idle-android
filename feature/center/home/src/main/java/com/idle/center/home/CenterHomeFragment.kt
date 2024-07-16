package com.idle.center.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
internal class CenterHomeFragment : BaseComposeFragment() {
    override val viewModel: CenterHomeViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        viewModel.apply {
            CenterHomeScreen()
        }
    }
}

@Composable
internal fun CenterHomeScreen(
) {
    Scaffold { paddingValue ->
        Box(
            modifier = Modifier.padding(paddingValue)
                .fillMaxWidth()
        ) {
            Text(
                text = "센터 홈...!",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}