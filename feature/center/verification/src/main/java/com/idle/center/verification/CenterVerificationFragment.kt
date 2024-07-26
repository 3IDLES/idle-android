package com.idle.center.verification

import android.net.Uri
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareSubtitleTopAppBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterVerificationFragment : BaseComposeFragment() {
    override val fragmentViewModel: CenterVerificationViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val verificationProcess by verificationProcess.collectAsStateWithLifecycle()
            val centerName by centerName.collectAsStateWithLifecycle()
            val centerNumber by centerNumber.collectAsStateWithLifecycle()
            val centerIntroduce by centerIntroduce.collectAsStateWithLifecycle()
            val centerProfileImageUri by centerProfileImageUri.collectAsStateWithLifecycle()

            CenterVerificationScreen(
                verificationProcess = verificationProcess,
                centerName = centerName,
                centerNumber = centerNumber,
                centerIntroduce = centerIntroduce,
                centerProfileImageUri = centerProfileImageUri,
                setVerificationProcess = ::setVerificationProcess,
                onCenterNameChanged = ::setCenterName,
                onCenterNumberChanged = ::setCenterNumber,
                onCenterIntroduceChanged = ::setCenterIntroduce,
            )
        }
    }
}


@Composable
internal fun CenterVerificationScreen(
    verificationProcess: VerificationProcess,
    centerName: String,
    centerNumber: String,
    centerIntroduce: String,
    centerProfileImageUri: Uri?,
    onCenterNameChanged: (String) -> Unit,
    onCenterNumberChanged: (String) -> Unit,
    onCenterIntroduceChanged: (String) -> Unit,
    setVerificationProcess: (VerificationProcess) -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current
    val (businessRegistrationProcessed, setBusinessRegistrationProcessed)
            = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CareSubtitleTopAppBar(
                title = "센터 회원가입",
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, bottom = 8.dp)
            )
        },
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(paddingValue)
                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp),
        ) {
            CareProgressBar(
                currentStep = verificationProcess.step,
                totalSteps = VerificationProcess.entries.size,
                modifier = Modifier.fillMaxWidth(),
            )

            AnimatedContent(
                targetState = verificationProcess,
                transitionSpec = {
                    if (targetState.ordinal > initialState.ordinal) {
                        slideInHorizontally(initialOffsetX = { it }) + fadeIn() togetherWith
                                slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                    } else {
                        slideInHorizontally(initialOffsetX = { -it }) + fadeIn() togetherWith
                                slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                    }
                },
                label = "센터의 회원가입을 관리하는 애니메이션",
            ) { verificationProcess ->
                when (verificationProcess) {
                    VerificationProcess.INFO -> Unit
                    VerificationProcess.ADDRESS -> Unit
                    VerificationProcess.INTRODUCE -> Unit
                }
            }
        }
    }
}