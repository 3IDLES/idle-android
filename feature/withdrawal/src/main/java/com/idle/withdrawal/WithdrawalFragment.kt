@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package com.idle.withdrawal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.idle.binding.base.CareBaseEvent
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.UserRole
import com.idle.withdrawal.step.PhoneNumberScreen
import com.idle.withdrawal.step.ReasonScreen
import dagger.hilt.android.AndroidEntryPoint

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
            val userRole by rememberSaveable { mutableStateOf(UserRole.create(args.userRole)) }

            WithdrawalStep(
                userRole = userRole,
                withdrawalStep = withdrawalStep,
                timerMinute = authCodeTimerMinute,
                timerSeconds = authCodeTimerSeconds,
                isConfirmAuthCode = isConfirmAuthCode,
                setWithdrawalStep = ::setWithdrawalStep,
                onReasonChanged = ::setWithdrawalReason,
                onPhoneNumberChanged = ::setPhoneNumber,
                onAuthCodeChanged = ::setAuthCode,
                sendPhoneNumber = ::sendPhoneNumber,
                confirmAuthCode = ::confirmAuthCode,
                withdrawal = ::withdrawal,
                navigateToSetting = {
                    baseEvent(
                        CareBaseEvent.NavigateTo(
                            destination = if (userRole == UserRole.CENTER) CenterSetting
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
internal fun WithdrawalStep(
    userRole: UserRole,
    withdrawalStep: WithdrawalStep,
    timerMinute: String,
    timerSeconds: String,
    isConfirmAuthCode: Boolean,
    setWithdrawalStep: (WithdrawalStep) -> Unit,
    onReasonChanged: (WithdrawalReason) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onAuthCodeChanged: (String) -> Unit,
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
                .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 28.dp),
        ) {
            CareStateAnimator(
                targetState = withdrawalStep,
                label = "회원 탈퇴를 관리하는 애니메이션",
            ) { withdrawalStep ->
                when (withdrawalStep) {
                    WithdrawalStep.REASON -> ReasonScreen(
                        userRole = userRole,
                        onReasonChanged = onReasonChanged,
                        setWithdrawalStep = setWithdrawalStep,
                        navigateToSetting = navigateToSetting,
                    )

                    WithdrawalStep.PHONENUMBER -> PhoneNumberScreen(
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
                }
            }
        }
    }
}