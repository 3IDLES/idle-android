package com.idle.signin.center.newpassword

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.center.newpassword.step.GenerateNewPasswordScreen
import com.idle.signin.center.newpassword.step.PhoneNumberScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPasswordFragment : BaseComposeFragment() {
    override val fragmentViewModel: NewPasswordViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val phoneNumber by phoneNumber.collectAsStateWithLifecycle()
            val authCode by authCode.collectAsStateWithLifecycle()
            val timerMinute by timerMinute.collectAsStateWithLifecycle()
            val timerSeconds by timerSeconds.collectAsStateWithLifecycle()
            val isConfirmAuthCode by isConfirmAuthCode.collectAsStateWithLifecycle()
            val newPasswordProcess by newPasswordProcess.collectAsStateWithLifecycle()
            val newPassword by newPassword.collectAsStateWithLifecycle()
            val newPasswordForConfirm by newPasswordForConfirm.collectAsStateWithLifecycle()

            NewPasswordScreen(
                snackbarHostState = snackbarHostState,
                newPasswordStep = newPasswordProcess,
                phoneNumber = phoneNumber,
                authCode = authCode,
                timerMinute = timerMinute,
                timerSeconds = timerSeconds,
                isConfirmAuthCode = isConfirmAuthCode,
                newPassword = newPassword,
                newPasswordForConfirm = newPasswordForConfirm,
                setNewPasswordProcess = ::setNewPasswordProcess,
                onPhoneNumberChanged = ::setPhoneNumber,
                onAuthCodeChanged = ::setAuthCode,
                sendPhoneNumber = ::sendPhoneNumber,
                confirmAuthCode = ::confirmAuthCode,
                onNewPasswordChanged = ::setNewPassword,
                onNewPasswordForConfirmChanged = ::setNewPasswordForConfirm,
            )
        }
    }
}

@Composable
internal fun NewPasswordScreen(
    snackbarHostState: SnackbarHostState,
    phoneNumber: String,
    authCode: String,
    timerMinute: String,
    timerSeconds: String,
    isConfirmAuthCode: Boolean,
    newPassword: String,
    newPasswordForConfirm: String,
    newPasswordStep: NewPasswordStep,
    onPhoneNumberChanged: (String) -> Unit,
    onAuthCodeChanged: (String) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
    onNewPasswordChanged: (String) -> Unit,
    onNewPasswordForConfirmChanged: (String) -> Unit,
    setNewPasswordProcess: (NewPasswordStep) -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CareSubtitleTopBar(
                title = stringResource(id = R.string.generate_new_password),
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, end = 20.dp, bottom = 12.dp),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) { data -> CareSnackBar(data = data) } },
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxSize()
                .background(CareTheme.colors.white000)
                .padding(paddingValue)
                .padding(start = 20.dp, end = 20.dp, top = 24.dp),
        ) {
            CareStateAnimator(
                targetState = newPasswordStep,
                label = "새 비밀번호 발급을 관리하는 애니메이션",
            ) { step ->
                when (step) {
                    NewPasswordStep.PHONE_NUMBER -> PhoneNumberScreen(
                        phoneNumber = phoneNumber,
                        authCode = authCode,
                        timerMinute = timerMinute,
                        timerSeconds = timerSeconds,
                        isConfirmAuthCode = isConfirmAuthCode,
                        onPhoneNumberChanged = onPhoneNumberChanged,
                        onAuthCodeChanged = onAuthCodeChanged,
                        sendPhoneNumber = sendPhoneNumber,
                        confirmAuthCode = confirmAuthCode,
                        setNewPasswordProcess = setNewPasswordProcess,
                    )

                    NewPasswordStep.GENERATE_NEW_PASSWORD -> GenerateNewPasswordScreen(
                        newPassword = newPassword,
                        newPasswordForConfirm = newPasswordForConfirm,
                        onNewPasswordChanged = onNewPasswordChanged,
                        onNewPasswordForConfirmChanged = onNewPasswordForConfirmChanged,
                        setNewPasswordProcess = setNewPasswordProcess,
                    )
                }
            }
        }
    }
}