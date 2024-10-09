package com.idle.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class SplashFragment : BaseComposeFragment() {
    override val fragmentViewModel: SplashViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {

            SplashScreen(snackbarHostState = snackbarHostState)
        }
    }
}

@Composable
internal fun SplashScreen(snackbarHostState: SnackbarHostState) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    CareSnackBar(
                        data = data,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }
            )
        },
        containerColor = CareTheme.colors.orange500,
    ) { paddingValue ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            Text(
                text = "빠른 요양보호사 매칭은",
                style = CareTheme.typography.subtitle2,
                color = CareTheme.colors.orange200,
                modifier = Modifier.padding(bottom = 2.dp),
            )

            Image(
                painter = painterResource(R.drawable.ic_splash_logo),
                contentDescription = null,
            )
        }
    }

    TrackScreenViewEvent(screenName = "splash_screen")
}
