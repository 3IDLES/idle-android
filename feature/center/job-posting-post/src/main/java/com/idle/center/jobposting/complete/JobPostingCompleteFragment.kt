package com.idle.center.jobposting.complete

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(CareTheme.colors.white000)
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.ic_check_with_circle),
            contentDescription = null,
            modifier = Modifier.padding(bottom = 36.dp),
        )

        Text(
            text = stringResource(id = R.string.job_posting_complete_message),
            style = CareTheme.typography.heading1,
            color = CareTheme.colors.black,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.confirm_short),
            onClick = { backPressedDispatcher?.onBackPressed() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }

    TrackScreenViewEvent(screenName = "jobposting_complete_screen")
}