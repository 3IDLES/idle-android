package com.idle.center.jobposting.complete

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.foundation.CareTheme

class JobPostingCompleteFragment : BaseComposeFragment() {

    override val fragmentViewModel: JobPostingCompleteViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            JobPostingCompleteScreen()
        }
    }
}

@Composable
internal fun JobPostingCompleteScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(CareTheme.colors.white000)
    ) {

    }
}