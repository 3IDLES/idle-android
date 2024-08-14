@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package com.idle.withdrawal

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.withdrawal.step.PhoneNumberScreen
import com.idle.withdrawal.step.ReasonScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WithdrawalFragment : BaseComposeFragment() {

    override val fragmentViewModel: WithdrawalViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val withdrawalStep by withdrawalStep.collectAsStateWithLifecycle()

            WithdrawalStep(
                withdrawalStep = withdrawalStep,
                setWithdrawalStep = ::setWithdrawalStep,
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
internal fun WithdrawalStep(
    withdrawalStep: WithdrawalStep,
    setWithdrawalStep: (WithdrawalStep) -> Unit,
) {
    val onBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CareSubtitleTopBar(
                title = stringResource(id = R.string.account_withdrawal),
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
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
                    WithdrawalStep.REASON -> ReasonScreen(setWithdrawalStep)
                    WithdrawalStep.PHONENUMBER -> PhoneNumberScreen(setWithdrawalStep)
                }
            }
        }
    }
}