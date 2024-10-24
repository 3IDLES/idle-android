package com.idle.signin.center

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.navArgs
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.binding.DeepLinkDestination.Auth
import com.idle.binding.DeepLinkDestination.NewPassword
import com.idle.binding.MainEvent
import com.idle.binding.NavigationEvent
import com.idle.binding.ToastType
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterSignInFragment : BaseComposeFragment() {
    private val args: CenterSignInFragmentArgs by navArgs()
    override val fragmentViewModel: CenterSignInViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val centerId by centerId.collectAsStateWithLifecycle()
            val centerPassword by centerPassword.collectAsStateWithLifecycle()
            val isLoginError by isLoginError.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                if (args.toastMsg != "default") {
                    eventHandlerHelper.sendEvent(
                        MainEvent.ShowToast(
                            args.toastMsg,
                            ToastType.SUCCESS
                        )
                    )
                }
            }

            CenterSignInScreen(
                centerId = centerId,
                centerPassword = centerPassword,
                isLoginError = isLoginError,
                onCenterIdChanged = ::setCenterId,
                onCenterPasswordChanged = ::setCenterPassword,
                signInCenter = ::signInCenter,
                navigateToAuth = {
                    navigationHelper.navigateTo(
                        NavigationEvent.NavigateTo(
                            destination = Auth,
                            popUpTo = com.idle.signin.R.id.centerSignInFragment
                        )
                    )
                },
                navigateToNewPassword = {
                    navigationHelper.navigateTo(
                        NavigationEvent.NavigateTo(NewPassword)
                    )
                }
            )
        }
    }
}


@Composable
internal fun CenterSignInScreen(
    centerId: String,
    centerPassword: String,
    isLoginError: Boolean,
    onCenterIdChanged: (String) -> Unit,
    onCenterPasswordChanged: (String) -> Unit,
    signInCenter: () -> Unit,
    navigateToAuth: () -> Unit = {},
    navigateToNewPassword: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

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
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .background(CareTheme.colors.white000)
                .padding(paddingValue)
                .padding(start = 20.dp, end = 20.dp, top = 24.dp),
        ) {
            Spacer(modifier = Modifier.weight(3f))

            LabeledContent(
                subtitle = stringResource(id = R.string.id),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
            ) {
                CareTextField(
                    value = centerId,
                    hint = stringResource(id = R.string.id_hint),
                    onValueChanged = onCenterIdChanged,
                    onDone = { if (centerId.isNotBlank()) focusManager.moveFocus(FocusDirection.Down) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LabeledContent(
                subtitle = stringResource(id = R.string.password),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    CareTextField(
                        value = centerPassword,
                        hint = stringResource(id = R.string.password_hint),
                        onValueChanged = onCenterPasswordChanged,
                        isError = isLoginError,
                        visualTransformation = PasswordVisualTransformation(),
                        onDone = { if (centerPassword.isNotBlank()) signInCenter() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 2.dp),
                    )

                    Text(
                        text = if (isLoginError) stringResource(R.string.login_error_description) else "",
                        style = CareTheme.typography.caption1,
                        color = CareTheme.colors.red,
                    )
                }
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