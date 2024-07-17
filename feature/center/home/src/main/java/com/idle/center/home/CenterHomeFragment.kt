package com.idle.center.home

import androidx.compose.foundation.background
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
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterHomeFragment : BaseComposeFragment() {
    override val fragmentViewModel: CenterHomeViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
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
                .fillMaxSize()
                .background(CareTheme.colors.red)
        ) {
            Text(
                text = "센터 홈...!",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}