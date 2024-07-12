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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.idle.binding.DeepLinkDestination.NewPassword
import com.idle.binding.deepLinkNavigateTo
import com.idle.binding.repeatOnStarted
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.CareTopAppBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.center.CenterSignInEvent.NavigateTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterSignInFragment : BaseComposeFragment() {
    override val viewModel: CenterSignInViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        viewModel.apply {
            viewLifecycleOwner.repeatOnStarted {
                eventFlow.collect { handleEvent(it) }
            }

            val centerId by centerId.collectAsStateWithLifecycle()
            val centerPassword by centerPassword.collectAsStateWithLifecycle()

            CenterSignInScreen(
                centerId = centerId,
                centerPassword = centerPassword,
                onCenterIdChanged = ::setCenterId,
                onCenterPasswordChanged = ::setCenterPassword,
                signInCenter = ::signInCenter,
                navigateToNewPassword = { event(NavigateTo(NewPassword)) }
            )
        }
    }

    private fun handleEvent(event: CenterSignInEvent) = when (event) {
        is NavigateTo -> findNavController().deepLinkNavigateTo(requireContext(), event.destination)
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
            CareTopAppBar(
                title = "로그인",
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp)
            )
        },
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            modifier = Modifier.fillMaxSize()
                .background(CareTheme.colors.white000)
                .padding(paddingValue)
                .padding(start = 20.dp, end = 20.dp, top = 125.dp, bottom = 30.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                modifier = Modifier.padding(bottom = 16.dp),
            ) {
                Text(
                    text = "아이디",
                    style = CareTheme.typography.subtitle4,
                    color = CareTheme.colors.gray500,
                )


                CareTextField(
                    value = centerId,
                    hint = "아이디를 입력해주세요.",
                    onValueChanged = onCenterIdChanged,
                    onDone = { },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
            ) {
                Text(
                    text = "비밀번호",
                    style = CareTheme.typography.subtitle4,
                    color = CareTheme.colors.gray500,
                )


                CareTextField(
                    value = centerPassword,
                    hint = "비밀번호를 입력해주세요.",
                    onValueChanged = onCenterPasswordChanged,
                    onDone = { },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "비밀번호가 기억나지 않나요?",
                textDecoration = TextDecoration.Underline,
                style = CareTheme.typography.body3,
                color = CareTheme.colors.gray500,
                modifier = Modifier.clickable { navigateToNewPassword() }
            )

            CareButtonLarge(
                text = "로그인",
                enable = centerPassword.isNotBlank(),
                onClick = signInCenter,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}