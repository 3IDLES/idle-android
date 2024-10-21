package com.idle.center.register

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.idle.binding.DeepLinkDestination
import com.idle.binding.EventHandler
import com.idle.binding.NavigationEvent
import com.idle.center.register.step.CenterAddressScreen
import com.idle.center.register.step.CenterInfoScreen
import com.idle.center.register.step.CenterIntroduceScreen
import com.idle.center.register.step.CenterRegisterSummaryScreen
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.post.code.PostCodeFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class RegisterCenterInfoFragment : BaseComposeFragment() {

    override val fragmentViewModel: RegisterCenterInfoViewModel by viewModels()

    @Inject
    lateinit var eventHandler: EventHandler

    private val postCodeDialog: PostCodeFragment? by lazy {
        PostCodeFragment().apply {
            onDismissCallback = {
                findNavController().currentBackStackEntry?.savedStateHandle?.let {
                    val roadNameAddress = it.get<String>("roadNameAddress")
                    val lotNumberAddress = it.get<String>("lotNumberAddress")

                    fragmentViewModel.setRoadNameAddress(roadNameAddress ?: return@let)
                    fragmentViewModel.setLotNumberAddress(lotNumberAddress ?: return@let)
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

            CareStateAnimator(
                targetState = registrationStep == RegistrationStep.SUMMARY,
                transitionCondition = registrationStep == RegistrationStep.SUMMARY
            ) { isSummary ->
                if (isSummary) {
                    CenterRegisterSummaryScreen(
                        centerName = centerName,
                        centerNumber = centerNumber,
                        centerIntroduce = centerIntroduce,
                        centerProfileImageUri = centerProfileImageUri,
                        roadNameAddress = roadNameAddress,
                        setRegistrationStep = ::setRegistrationStep,
                        registerCenterProfile = ::registerCenterProfile,
                    )
                } else {
                    CenterRegisterScreen(
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
                        navigateToHome = {
                            navigationRouter.navigateTo(
                                NavigationEvent.NavigateTo(
                                    DeepLinkDestination.CenterHome,
                                    com.idle.center.register.info.R.id.registerCenterInfoCompleteFragment
                                )
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
internal fun CenterRegisterScreen(
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
    navigateToHome: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(start = 12.dp, top = 48.dp, end = 20.dp)) {
                CareSubtitleTopBar(
                    title = stringResource(id = R.string.register_center_info),
                    onNavigationClick = navigateToHome,
                    modifier = Modifier.fillMaxWidth(),
                )

                CareProgressBar(
                    currentStep = registrationStep.step,
                    totalSteps = RegistrationStep.entries.size - 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
                )
            }
        },
        containerColor = CareTheme.colors.white000,
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxSize()
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
                    )

                    else -> Box(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}