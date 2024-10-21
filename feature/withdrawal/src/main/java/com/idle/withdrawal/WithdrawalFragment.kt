@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package com.idle.withdrawal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.navArgs
import com.idle.binding.DeepLinkDestination.CenterSetting
import com.idle.binding.DeepLinkDestination.WorkerSetting
import com.idle.binding.base.EventHandler
import com.idle.binding.base.MainEvent
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareDialog
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.UserType
import com.idle.withdrawal.step.PasswordScreen
import com.idle.withdrawal.step.PhoneNumberScreen
import com.idle.withdrawal.step.ReasonScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class WithdrawalFragment : BaseComposeFragment() {
    private val args: WithdrawalFragmentArgs by navArgs()
    override val fragmentViewModel: WithdrawalViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val withdrawalStep by withdrawalStep.collectAsStateWithLifecycle()
            val authCodeTimerMinute by authCodeTimerMinute.collectAsStateWithLifecycle()
            val authCodeTimerSeconds by authCodeTimerSeconds.collectAsStateWithLifecycle()
            val isConfirmAuthCode by isConfirmAuthCode.collectAsStateWithLifecycle()
            val inconvenientReason by inconvenientReason.collectAsStateWithLifecycle()
            val anotherPlatformReason by anotherPlatformReason.collectAsStateWithLifecycle()
            val lackFeaturesReason by lackFeaturesReason.collectAsStateWithLifecycle()
            val password by password.collectAsStateWithLifecycle()
            val userType by rememberSaveable { mutableStateOf(UserType.create(args.userType)) }
            var showDialog by rememberSaveable { mutableStateOf(false) }

            if (showDialog) {
                CareDialog(
                    title = "정말 탈퇴하시겠어요?",
                    description = "탈퇴 버튼 선택 시 모든 정보가 삭제되며,\n" +
                            "되돌릴 수 없습니다.",
                    leftButtonText = stringResource(id = R.string.cancel_short),
                    rightButtonText = stringResource(id = R.string.withdrawal),
                    leftButtonTextColor = CareTheme.colors.gray300,
                    leftButtonColor = CareTheme.colors.white000,
                    leftButtonBorder = BorderStroke(1.dp, CareTheme.colors.gray100),
                    rightButtonTextColor = CareTheme.colors.white000,
                    rightButtonColor = CareTheme.colors.red,
                    onDismissRequest = { showDialog = false },
                    onLeftButtonClick = { showDialog = false },
                    onRightButtonClick = {
                        withdrawal(userType)
                        showDialog = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                )
            }

            WithdrawalScreen(
                userType = userType,
                withdrawalStep = withdrawalStep,
                timerMinute = authCodeTimerMinute,
                timerSeconds = authCodeTimerSeconds,
                isConfirmAuthCode = isConfirmAuthCode,
                inconvenientReason = inconvenientReason,
                anotherPlatformReason = anotherPlatformReason,
                lackFeaturesReason = lackFeaturesReason,
                password = password,
                setWithdrawalStep = ::setWithdrawalStep,
                onReasonChanged = ::setWithdrawalReason,
                onInconvenientReasonChanged = ::setInconvenientReason,
                onAnotherPlatformReasonChanged = ::setAnotherPlatformReason,
                onLackFeaturesReasonChanged = ::setLackFeaturesReason,
                onPhoneNumberChanged = ::setPhoneNumber,
                onAuthCodeChanged = ::setAuthCode,
                onPasswordChanged = ::setPassword,
                sendPhoneNumber = ::sendPhoneNumber,
                confirmAuthCode = ::confirmAuthCode,
                withdrawal = { showDialog = true },
                navigateToSetting = {
                    eventHandler.sendEvent(
                        MainEvent.NavigateTo(
                            destination = if (userType == UserType.CENTER) CenterSetting
                            else WorkerSetting,
                            popUpTo = com.idle.withdrawal.R.id.withdrawalFragment,
                        )
                    )
                },
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
internal fun WithdrawalScreen(
    userType: UserType,
    withdrawalStep: WithdrawalStep,
    timerMinute: String,
    timerSeconds: String,
    isConfirmAuthCode: Boolean,
    inconvenientReason: String,
    anotherPlatformReason: String,
    lackFeaturesReason: String,
    password: String,
    setWithdrawalStep: (WithdrawalStep) -> Unit,
    onReasonChanged: (WithdrawalReason) -> Unit,
    onInconvenientReasonChanged: (String) -> Unit,
    onAnotherPlatformReasonChanged: (String) -> Unit,
    onLackFeaturesReasonChanged: (String) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onAuthCodeChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
    withdrawal: () -> Unit,
    navigateToSetting: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CareSubtitleTopBar(
                title = stringResource(id = R.string.account_withdrawal),
                onNavigationClick = navigateToSetting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        top = 48.dp,
                        end = 20.dp,
                        bottom = 12.dp
                    ),
            )
        },
        containerColor = CareTheme.colors.white000,
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValue)
                .padding(start = 20.dp, end = 20.dp, top = 24.dp),
        ) {
            CareStateAnimator(
                targetState = withdrawalStep,
                label = "회원 탈퇴를 관리하는 애니메이션",
            ) { withdrawalStep ->
                when (withdrawalStep) {
                    WithdrawalStep.REASON -> ReasonScreen(
                        userType = userType,
                        inconvenientReason = inconvenientReason,
                        anotherPlatformReason = anotherPlatformReason,
                        lackFeaturesReason = lackFeaturesReason,
                        onReasonChanged = onReasonChanged,
                        setWithdrawalStep = setWithdrawalStep,
                        onInconvenientReasonChanged = onInconvenientReasonChanged,
                        onAnotherPlatformReasonChanged = onAnotherPlatformReasonChanged,
                        onLackFeaturesReasonChanged = onLackFeaturesReasonChanged,
                        navigateToSetting = navigateToSetting,
                    )

                    WithdrawalStep.PHONE_NUMBER -> PhoneNumberScreen(
                        timerMinute = timerMinute,
                        timerSeconds = timerSeconds,
                        isConfirmAuthCode = isConfirmAuthCode,
                        onPhoneNumberChanged = onPhoneNumberChanged,
                        onAuthCodeChanged = onAuthCodeChanged,
                        sendPhoneNumber = sendPhoneNumber,
                        confirmAuthCode = confirmAuthCode,
                        setWithdrawalStep = setWithdrawalStep,
                        withdrawal = withdrawal,
                        navigateToSetting = navigateToSetting,
                    )

                    WithdrawalStep.PASSWORD -> PasswordScreen(
                        password = password,
                        onPasswordChanged = onPasswordChanged,
                        setWithdrawalStep = setWithdrawalStep,
                        withdrawal = withdrawal,
                        navigateToSetting = navigateToSetting,
                    )
                }
            }
        }
    }
}