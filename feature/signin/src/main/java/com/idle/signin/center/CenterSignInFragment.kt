package com.idle.signin.center

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.binding.DeepLinkDestination.Auth
import com.idle.binding.DeepLinkDestination.NewPassword
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterSignInFragment : BaseComposeFragment() {
    override val fragmentViewModel: CenterSignInViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val centerId by centerId.collectAsStateWithLifecycle()
            val centerPassword by centerPassword.collectAsStateWithLifecycle()

            CenterSignInScreen(
                snackbarHostState = snackbarHostState,
                centerId = centerId,
                centerPassword = centerPassword,
                onCenterIdChanged = ::setCenterId,
                onCenterPasswordChanged = ::setCenterPassword,
                signInCenter = ::signInCenter,
                navigateToAuth = {
                    baseEvent(
                        NavigateTo(
                            destination = Auth,
                            popUpTo = com.idle.signin.R.id.centerSignInFragment
                        )
                    )
                },
                navigateToNewPassword = { baseEvent(NavigateTo(NewPassword)) }
            )
        }
    }
}


@Composable
internal fun CenterSignInScreen(
    snackbarHostState: SnackbarHostState,
    centerId: String,
    centerPassword: String,
    onCenterIdChanged: (String) -> Unit,
    onCenterPasswordChanged: (String) -> Unit,
    signInCenter: () -> Unit,
    navigateToAuth: () -> Unit = {},
    navigateToNewPassword: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    BackHandler { navigateToAuth() }

    Scaffold(
        topBar = {
            CareSubtitleTopBar(
                title = stringResource(id = R.string.login),
                onNavigationClick = { navigateToAuth() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, end = 20.dp, bottom = 12.dp),
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    CareSnackBar(
                        data = data,
                        modifier = Modifier.padding(bottom = 138.dp)
                    )
                }
            )
        },
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(CareTheme.colors.white000)
                .padding(paddingValue)
                .padding(start = 20.dp, end = 20.dp, top = 24.dp),
        ) {
            Spacer(modifier = Modifier.weight(2f))

            LabeledContent(
                subtitle = stringResource(id = R.string.id),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
            ) {
                CareTextField(
                    value = centerId,
                    hint = stringResource(id = R.string.id_hint),
                    onValueChanged = onCenterIdChanged,
                    onDone = { if (centerId.isNotBlank()) focusManager.moveFocus(FocusDirection.Down) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                )
            }

            LabeledContent(
                subtitle = stringResource(id = R.string.password),
                modifier = Modifier.fillMaxWidth(),
            ) {
                CareTextField(
                    value = centerPassword,
                    hint = stringResource(id = R.string.password_hint),
                    onValueChanged = onCenterPasswordChanged,
                    visualTransformation = PasswordVisualTransformation(),
                    onDone = { if (centerPassword.isNotBlank()) signInCenter() },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.weight(5f))

            Text(
                text = stringResource(id = R.string.forget_password),
                textDecoration = TextDecoration.Underline,
                style = CareTheme.typography.body3,
                color = CareTheme.colors.gray500,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .clickable { navigateToNewPassword() },
            )

            CareButtonLarge(
                text = stringResource(id = R.string.login),
                enable = centerPassword.isNotBlank(),
                onClick = signInCenter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
            )
        }
    }

    TrackScreenViewEvent(screenName = "center_signin_screen")
}