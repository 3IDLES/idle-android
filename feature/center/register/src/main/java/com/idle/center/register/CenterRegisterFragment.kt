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
import androidx.navigation.fragment.findNavController
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopAppBar
import com.idle.post.code.PostCodeFragment
import com.idle.signup.center.process.CenterAddressScreen
import com.idle.signup.center.process.CenterInfoScreen
import com.idle.signup.center.process.CenterIntroduceScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterRegisterFragment : BaseComposeFragment() {

    override val fragmentViewModel: CenterVerificationViewModel by viewModels()

    private val postCodeDialog: PostCodeFragment? by lazy {
        PostCodeFragment().apply {
            onDismissCallback =
                {
                    findNavController().currentBackStackEntry?.savedStateHandle?.let {
                        val roadNameAddress = it.get<String>("roadNameAddress")
                        val lotNumberAddress = it.get<String>("lotNumberAddress")

                        fragmentViewModel.setRoadNameAddress(roadNameAddress ?: "")
                        fragmentViewModel.setLotNumberAddress(lotNumberAddress ?: "")
                    }
                }
        }
    }

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val registerProcess by registerProcess.collectAsStateWithLifecycle()
            val centerName by centerName.collectAsStateWithLifecycle()
            val centerNumber by centerNumber.collectAsStateWithLifecycle()
            val centerIntroduce by centerIntroduce.collectAsStateWithLifecycle()
            val centerProfileImageUri by centerProfileImageUri.collectAsStateWithLifecycle()
            val roadNameAddress by roadNameAddress.collectAsStateWithLifecycle()
            val centerDetailAddress by centerDetailAddress.collectAsStateWithLifecycle()

            CenterRegisterScreen(
                registerProcess = registerProcess,
                centerName = centerName,
                centerNumber = centerNumber,
                centerIntroduce = centerIntroduce,
                centerProfileImageUri = centerProfileImageUri,
                roadNameAddress = roadNameAddress,
                centerDetailAddress = centerDetailAddress,
                setRegisterProcess = ::setRegisterProcess,
                onCenterNameChanged = ::setCenterName,
                onCenterNumberChanged = ::setCenterNumber,
                onCenterIntroduceChanged = ::setCenterIntroduce,
                showPostCodeDialog = {
                    if (!(postCodeDialog?.isAdded == true || postCodeDialog?.isVisible == true)) {
                        postCodeDialog?.show(parentFragmentManager, "PostCodeFragment")
                    }
                },
                onCenterDetailAddressChanged = ::setCenterDetailAddress,
                onProfileImageUriChanged = ::setProfileImageUri,
                registerCenterProfile = ::registerCenterProfile,
            )
        }
    }
}


@Composable
internal fun CenterRegisterScreen(
    registerProcess: RegisterProcess,
    centerName: String,
    centerNumber: String,
    centerIntroduce: String,
    centerProfileImageUri: Uri?,
    roadNameAddress: String,
    centerDetailAddress: String,
    onCenterNameChanged: (String) -> Unit,
    onCenterNumberChanged: (String) -> Unit,
    onCenterIntroduceChanged: (String) -> Unit,
    showPostCodeDialog: () -> Unit,
    onCenterDetailAddressChanged: (String) -> Unit,
    onProfileImageUriChanged: (Uri?) -> Unit,
    setRegisterProcess: (RegisterProcess) -> Unit,
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
                currentStep = registerProcess.step,
                totalSteps = RegisterProcess.entries.size,
                modifier = Modifier.fillMaxWidth(),
            )

            CareStateAnimator(
                targetState = registerProcess,
                label = "센터 정보 입력을 관리하는 애니메이션",
            ) { verificationProcess ->
                when (verificationProcess) {
                    RegisterProcess.INFO -> CenterInfoScreen(
                        centerName = centerName,
                        centerNumber = centerNumber,
                        onCenterNameChanged = onCenterNameChanged,
                        onCenterNumberChanged = onCenterNumberChanged,
                        setRegisterProcess = setRegisterProcess
                    )

                    RegisterProcess.ADDRESS -> CenterAddressScreen(
                        roadNameAddress = roadNameAddress,
                        centerDetailAddress = centerDetailAddress,
                        navigateToPostCode = showPostCodeDialog,
                        onCenterDetailAddressChanged = onCenterDetailAddressChanged,
                        setRegisterProcess = setRegisterProcess,
                    )

                    RegisterProcess.INTRODUCE -> CenterIntroduceScreen(
                        centerIntroduce = centerIntroduce,
                        centerProfileImageUri = centerProfileImageUri,
                        onCenterIntroduceChanged = onCenterIntroduceChanged,
                        onProfileImageUriChanged = onProfileImageUriChanged,
                        setRegisterProcess = setRegisterProcess,
                        registerCenterProfile = registerCenterProfile,
                    )
                }
            }
        }
    }
}