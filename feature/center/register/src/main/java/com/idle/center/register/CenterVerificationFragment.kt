package com.idle.center.register

import android.net.Uri
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopAppBar
import com.idle.signup.center.process.CenterAddressScreen
import com.idle.signup.center.process.CenterInfoScreen
import com.idle.signup.center.process.CenterIntroduceScreen
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
            val centerAddress by centerAddress.collectAsStateWithLifecycle()
            val centerDetailAddress by centerDetailAddress.collectAsStateWithLifecycle()

            CenterVerificationScreen(
                verificationProcess = verificationProcess,
                centerName = centerName,
                centerNumber = centerNumber,
                centerIntroduce = centerIntroduce,
                centerProfileImageUri = centerProfileImageUri,
                centerAddress = centerAddress,
                centerDetailAddress = centerDetailAddress,
                setVerificationProcess = ::setVerificationProcess,
                onCenterNameChanged = ::setCenterName,
                onCenterNumberChanged = ::setCenterNumber,
                onCenterIntroduceChanged = ::setCenterIntroduce,
                onCenterDetailAddressChanged = ::setCenterDetailAddress,
                onProfileImageUriChanged = ::setProfileImageUri,
                registerCenterProfile = ::registerCenterProfile,
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
    centerAddress: String,
    centerDetailAddress: String,
    onCenterNameChanged: (String) -> Unit,
    onCenterNumberChanged: (String) -> Unit,
    onCenterIntroduceChanged: (String) -> Unit,
    onCenterDetailAddressChanged: (String) -> Unit,
    onProfileImageUriChanged: (Uri?) -> Unit,
    setVerificationProcess: (VerificationProcess) -> Unit,
    registerCenterProfile: () -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current

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

            CareStateAnimator(
                targetState = verificationProcess,
                label = "센터 정보 입력을 관리하는 애니메이션",
            ) { verificationProcess ->
                when (verificationProcess) {
                    VerificationProcess.INFO -> CenterInfoScreen(
                        centerName = centerName,
                        centerNumber = centerNumber,
                        onCenterNameChanged = onCenterNameChanged,
                        onCenterNumberChanged = onCenterNumberChanged,
                        setVerificationProcess = setVerificationProcess
                    )

                    VerificationProcess.ADDRESS -> CenterAddressScreen(
                        centerAddress = centerAddress,
                        centerDetailAddress = centerDetailAddress,
                        onCenterDetailAddressChanged = onCenterDetailAddressChanged,
                        setVerificationProcess = setVerificationProcess,
                    )

                    VerificationProcess.INTRODUCE -> CenterIntroduceScreen(
                        centerIntroduce = centerIntroduce,
                        centerProfileImageUri = centerProfileImageUri,
                        onCenterIntroduceChanged = onCenterIntroduceChanged,
                        onProfileImageUriChanged = onProfileImageUriChanged,
                        setVerificationProcess = setVerificationProcess,
                        registerCenterProfile = registerCenterProfile,
                    )
                }
            }
        }
    }
}