package com.idle.signin.center.newpassword

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareTopAppBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.center.newpassword.process.GenerateNewPasswordScreen
import com.idle.signin.center.newpassword.process.PhoneNumberScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPasswordFragment : BaseComposeFragment() {
    override val fragmentViewModel: NewPasswordViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val phoneNumber by phoneNumber.collectAsStateWithLifecycle()
            val authCode by centerAuthCode.collectAsStateWithLifecycle()
            val generateNewPasswordProcess by generateNewPasswordProcess.collectAsStateWithLifecycle()
            val newPassword by newPassword.collectAsStateWithLifecycle()
            val newPasswordForConfirm by newPasswordForConfirm.collectAsStateWithLifecycle()

            NewPasswordScreen(
                generateNewPasswordProcess = generateNewPasswordProcess,
                phoneNumber = phoneNumber,
                certificationNumber = authCode,
                newPassword = newPassword,
                newPasswordForConfirm = newPasswordForConfirm,
                setGenerateNewPasswordProcess = ::setGenerateNewPasswordProcess,
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
    phoneNumber: String,
    certificationNumber: String,
    newPassword: String,
    newPasswordForConfirm: String,
    generateNewPasswordProcess: GenerateNewPasswordProcess,
    onPhoneNumberChanged: (String) -> Unit,
    onAuthCodeChanged: (String) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
    onNewPasswordChanged: (String) -> Unit,
    onNewPasswordForConfirmChanged: (String) -> Unit,
    setGenerateNewPasswordProcess: (GenerateNewPasswordProcess) -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CareTopAppBar(
                title = "신규 비밀번호 발급",
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, bottom = 64.dp)
            )
        },
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
            modifier = Modifier.fillMaxSize()
                .background(CareTheme.colors.white000)
                .padding(paddingValue)
                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp),
        ) {
            when (generateNewPasswordProcess) {
                GenerateNewPasswordProcess.PHONE_NUMBER -> PhoneNumberScreen(
                    phoneNumber = phoneNumber,
                    certificationNumber = certificationNumber,
                    onPhoneNumberChanged = onPhoneNumberChanged,
                    onAuthCodeChanged = onAuthCodeChanged,
                    sendPhoneNumber = sendPhoneNumber,
                    confirmAuthCode = confirmAuthCode,
                    setGenerateNewPasswordProcess = setGenerateNewPasswordProcess,
                )

                GenerateNewPasswordProcess.GENERATE_NEW_PASSWORD -> GenerateNewPasswordScreen(
                    newPassword = newPassword,
                    newPasswordForConfirm = newPasswordForConfirm,
                    onNewPasswordChanged = onNewPasswordChanged,
                    onNewPasswordForConfirmChanged = onNewPasswordForConfirmChanged,
                    setGenerateNewPasswordProcess = setGenerateNewPasswordProcess,
                )
            }
        }
    }
}