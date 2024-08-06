package com.idle.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.idle.binding.DeepLinkDestination.CenterAuth
import com.idle.binding.DeepLinkDestination.CenterHome
import com.idle.binding.DeepLinkDestination.WorkerAuth
import com.idle.binding.DeepLinkDestination.WorkerHome
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R.string
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class AuthFragment : BaseComposeFragment() {
    override val fragmentViewModel: AuthViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {

        AuthScreen(
            navigateToWorkerAuth = {
                fragmentViewModel.handleTokenNavigation(
                    defaultDestination = NavigateTo(WorkerAuth),
                    authenticatedDestination = NavigateTo(WorkerHome, R.id.authFragment),
                )
            },
            navigateToCenterAuth = {
                fragmentViewModel.handleTokenNavigation(
                    defaultDestination = NavigateTo(CenterAuth),
                    authenticatedDestination = NavigateTo(CenterHome, R.id.authFragment),
                )
            },
        )
    }
}


@Composable
internal fun AuthScreen(
    navigateToWorkerAuth: () -> Unit = {},
    navigateToCenterAuth: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = "어플 소개 한 줄 정도\n그리고 어플 이름",
            style = CareTheme.typography.heading1,
            color = CareTheme.colors.gray900,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 142.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                    alignment = Alignment.CenterVertically
                ),
                modifier = Modifier
                    .height(208.dp)
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = CareTheme.colors.gray100,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { navigateToCenterAuth() }
                    .padding(horizontal = 25.5.dp, vertical = 37.dp),
            ) {
                Text(
                    text = stringResource(id = string.start_center),
                    style = CareTheme.typography.subtitle2,
                    color = CareTheme.colors.gray900,
                    textAlign = TextAlign.Center,
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                    alignment = Alignment.CenterVertically
                ),
                modifier = Modifier
                    .height(208.dp)
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = CareTheme.colors.gray100,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { navigateToWorkerAuth() }
                    .padding(horizontal = 25.5.dp, vertical = 37.dp),
            ) {
                Text(
                    text = stringResource(id = string.start_worker),
                    style = CareTheme.typography.subtitle2,
                    color = CareTheme.colors.gray900,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}