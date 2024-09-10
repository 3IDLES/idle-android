package com.idle.signup.worker.complete

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
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.CareBaseEvent
import com.idle.center.jobposting.complete.SignUpCompleteViewModel
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpCompleteFragment : BaseComposeFragment() {
    override val fragmentViewModel: SignUpCompleteViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            SignUpCompleteScreen(
                navigateTo = {
                    baseEvent(
                        CareBaseEvent.NavigateTo(
                            destination = it,
                            popUpTo = com.idle.signup.R.id.signUpCompleteFragment,
                        )
                    )
                },
            )
        }
    }
}

@Composable
internal fun SignUpCompleteScreen(
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
            text = stringResource(id = R.string.worker_signup_complete_message),
            style = CareTheme.typography.heading1,
            color = CareTheme.colors.gray900,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        Text(
            text = stringResource(id = R.string.worker_signup_welcome_message),
            style = CareTheme.typography.body3,
            color = CareTheme.colors.gray500,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.start),
            onClick = { navigateTo(DeepLinkDestination.CenterHome) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }

    TrackScreenViewEvent(screenName = "signup_complete_screen")
}