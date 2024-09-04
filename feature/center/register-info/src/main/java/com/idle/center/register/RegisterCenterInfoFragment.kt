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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.idle.center.register.step.CenterAddressScreen
import com.idle.center.register.step.CenterInfoScreen
import com.idle.center.register.step.CenterIntroduceScreen
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.post.code.PostCodeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class RegisterCenterInfoFragment : BaseComposeFragment() {

    override val fragmentViewModel: RegisterCenterInfoViewModel by viewModels()

    private val postCodeDialog: PostCodeFragment? by lazy {
        PostCodeFragment().apply {
            onDismissCallback = {
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
            val registrationStep by registrationStep.collectAsStateWithLifecycle()
            val centerName by centerName.collectAsStateWithLifecycle()
            val centerNumber by centerNumber.collectAsStateWithLifecycle()
            val centerIntroduce by centerIntroduce.collectAsStateWithLifecycle()
            val centerProfileImageUri by centerProfileImageUri.collectAsStateWithLifecycle()
            val roadNameAddress by roadNameAddress.collectAsStateWithLifecycle()
            val centerDetailAddress by centerDetailAddress.collectAsStateWithLifecycle()

            CenterRegisterScreen(
                snackbarHostState = snackbarHostState,
                registrationStep = registrationStep,
                centerName = centerName,
                centerNumber = centerNumber,
                centerIntroduce = centerIntroduce,
                centerProfileImageUri = centerProfileImageUri,
                roadNameAddress = roadNameAddress,
                centerDetailAddress = centerDetailAddress,
                setRegistrationStep = ::setRegistrationStep,
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
    snackbarHostState: SnackbarHostState,
    registrationStep: RegistrationStep,
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
    setRegistrationStep: (RegistrationStep) -> Unit,
    registerCenterProfile: () -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(start = 12.dp, top = 48.dp, end = 20.dp)) {
                CareSubtitleTopBar(
                    title = "센터 정보 등록",
                    onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                    modifier = Modifier.fillMaxWidth(),
                )

                CareProgressBar(
                    currentStep = registrationStep.step,
                    totalSteps = RegistrationStep.entries.size,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    CareSnackBar(
                        data = data,
                        modifier = Modifier.padding(bottom = 138.dp)
                    )
                }
            )
        },
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValue)
                .padding(start = 20.dp, end = 20.dp, top = 24.dp),
        ) {
            CareStateAnimator(
                targetState = registrationStep,
                label = stringResource(id = R.string.center_info_input_animation_label),
            ) { registrationStep ->
                when (registrationStep) {
                    RegistrationStep.INFO -> CenterInfoScreen(
                        centerName = centerName,
                        centerNumber = centerNumber,
                        onCenterNameChanged = onCenterNameChanged,
                        onCenterNumberChanged = onCenterNumberChanged,
                        setRegistrationStep = setRegistrationStep
                    )

                    RegistrationStep.ADDRESS -> CenterAddressScreen(
                        roadNameAddress = roadNameAddress,
                        centerDetailAddress = centerDetailAddress,
                        showPostCode = showPostCodeDialog,
                        onCenterDetailAddressChanged = onCenterDetailAddressChanged,
                        setRegistrationStep = setRegistrationStep,
                    )

                    RegistrationStep.INTRODUCE -> CenterIntroduceScreen(
                        centerIntroduce = centerIntroduce,
                        centerProfileImageUri = centerProfileImageUri,
                        onCenterIntroduceChanged = onCenterIntroduceChanged,
                        onProfileImageUriChanged = onProfileImageUriChanged,
                        setRegistrationStep = setRegistrationStep,
                        registerCenterProfile = registerCenterProfile,
                    )
                }
            }
        }
    }
}