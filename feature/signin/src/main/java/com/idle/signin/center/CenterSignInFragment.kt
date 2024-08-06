package com.idle.signin.center

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.binding.DeepLinkDestination.NewPassword
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareSubtitleTopAppBar
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
                centerId = centerId,
                centerPassword = centerPassword,
                onCenterIdChanged = ::setCenterId,
                onCenterPasswordChanged = ::setCenterPassword,
                signInCenter = ::signInCenter,
                navigateToNewPassword = { baseEvent(NavigateTo(NewPassword)) }
            )
        }
    }
}


@Composable
internal fun CenterSignInScreen(
    centerId: String,
    centerPassword: String,
    onCenterIdChanged: (String) -> Unit,
    onCenterPasswordChanged: (String) -> Unit,
    signInCenter: () -> Unit,
    navigateToNewPassword: () -> Unit = {},
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CareSubtitleTopAppBar(
                title = stringResource(id = R.string.login),
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp)
            )
        },
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxSize()
                .background(CareTheme.colors.white000)
                .padding(paddingValue)
                .padding(start = 20.dp, end = 20.dp, top = 125.dp, bottom = 30.dp),
        ) {
            LabeledContent(
                subtitle = stringResource(id = R.string.id),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            ) {
                CareTextField(
                    value = centerId,
                    hint = stringResource(id = R.string.id_hint),
                    onValueChanged = onCenterIdChanged,
                    onDone = { },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            LabeledContent(
                subtitle = stringResource(id = R.string.password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
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

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.forget_password),
                textDecoration = TextDecoration.Underline,
                style = CareTheme.typography.body3,
                color = CareTheme.colors.gray500,
                modifier = Modifier.clickable { navigateToNewPassword() }
            )

            CareButtonLarge(
                text = stringResource(id = R.string.login),
                enable = centerPassword.isNotBlank(),
                onClick = signInCenter,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}