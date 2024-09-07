package com.idle.pending

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterPendingFragment : BaseComposeFragment() {
    override val fragmentViewModel: CenterPendingViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {

            CenterPendingScreen(
                navigateTo = { baseEvent(NavigateTo(it)) }
            )
        }
    }
}

@Composable
internal fun CenterPendingScreen(
    navigateTo: (DeepLinkDestination) -> Unit,
) {
    Scaffold(
        containerColor = CareTheme.colors.white000,
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxSize()
        ) {
        }
    }
}