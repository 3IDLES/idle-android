package com.idle.center.jobposting.complete

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
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.CareBaseEvent
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.foundation.CareTheme

class JobPostingCompleteFragment : BaseComposeFragment() {

    override val fragmentViewModel: JobPostingCompleteViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            JobPostingCompleteScreen(
                navigateTo = {
                    baseEvent(
                        CareBaseEvent.NavigateTo(
                            destination = it,
                            popUpTo = com.idle.center.job.posting.R.id.jobPostingCompleteFragment
                        )
                    )
                },
            )
        }
    }
}

@Composable
internal fun JobPostingCompleteScreen(
    navigateTo: (DeepLinkDestination) -> Unit,
) {
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
            color = CareTheme.colors.gray900,
            textAlign = TextAlign. Center,
        )

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.confirm),
            onClick = { navigateTo(DeepLinkDestination.CenterHome) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }
}