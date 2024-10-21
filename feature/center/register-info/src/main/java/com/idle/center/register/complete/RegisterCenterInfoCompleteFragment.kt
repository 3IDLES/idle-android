package com.idle.center.register.complete

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.binding.DeepLinkDestination.CenterHome
import com.idle.binding.NavigationEvent
import com.idle.center.register.info.R
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterRegisterCompleteFragment : BaseComposeFragment() {
    override val fragmentViewModel: RegisterCenterInfoCompleteViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val centerProfile by centerProfile.collectAsStateWithLifecycle()

            centerProfile?.let {
                CenterRegisterCompleteScreen(
                    navigateToCenterHome = {
                        navigationRouter.navigateTo(
                            NavigationEvent.NavigateTo(
                                CenterHome,
                                R.id.registerCenterInfoCompleteFragment
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
internal fun CenterRegisterCompleteScreen(navigateToCenterHome: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(CareTheme.colors.white000)
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = com.idle.designresource.R.drawable.ic_check_with_circle),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.size(120.dp),
        )

        Text(
            text = stringResource(id = com.idle.designresource.R.string.register_center_complete),
            style = CareTheme.typography.heading1,
            color = CareTheme.colors.black,
            modifier = Modifier.padding(top = 24.dp),
        )

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = com.idle.designresource.R.string.confirm_short),
            onClick = navigateToCenterHome,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }

    TrackScreenViewEvent(screenName = "center_register_info_complete_screen")
}