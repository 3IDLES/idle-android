package com.idle.center.profile

import android.util.Log
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
internal class CenterProfileFragment : BaseComposeFragment() {
    override val fragmentViewModel: CenterProfileViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            CenterProfileScreen()
        }
    }
}

@Composable
internal fun CenterProfileScreen(
) {
    Scaffold { paddingValue ->
        Box(
            modifier = Modifier.padding(paddingValue)
                .fillMaxSize()
        ) {
            Text(
                text = "센터 프로필...!",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}