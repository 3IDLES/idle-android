package com.idle.signin.center

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
import androidx.navigation.fragment.findNavController
import com.idle.binding.deepLinkNavigateTo
import com.idle.binding.repeatOnStarted
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareTopAppBar
import com.idle.domain.model.auth.BusinessRegistrationInfo
import com.idle.signup.center.process.BusinessRegistrationScreen
import com.idle.signup.center.process.CenterNameScreen
import com.idle.signup.center.process.CenterPhoneNumberScreen
import com.idle.signup.center.process.IdPasswordScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterSignUpFragment : BaseComposeFragment() {
    override val viewModel: CenterSignUpViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        viewModel.apply {
            viewLifecycleOwner.repeatOnStarted {
                eventFlow.collect { handleEvent(it) }
            }

            val signUpProcess by signUpProcess.collectAsStateWithLifecycle()
            val centerName by centerName.collectAsStateWithLifecycle()
            val centerPhoneNumber by centerPhoneNumber.collectAsStateWithLifecycle()
            val centerAuthCodeTimerMinute by centerAuthCodeTimerMinute.collectAsStateWithLifecycle()
            val centerAuthCodeTimerSeconds by centerAuthCodeTimerSeconds.collectAsStateWithLifecycle()
            val centerAuthCode by centerAuthCode.collectAsStateWithLifecycle()
            val isConfirmAuthCode by isConfirmAuthCode.collectAsStateWithLifecycle()
            val businessRegistrationNumber
                    by businessRegistrationNumber.collectAsStateWithLifecycle()
            val businessRegistrationInfo by businessRegistrationInfo.collectAsStateWithLifecycle()
            val centerId by centerId.collectAsStateWithLifecycle()
            val centerIdResult by centerIdResult.collectAsStateWithLifecycle()
            val centerPassword by centerPassword.collectAsStateWithLifecycle()
            val centerPasswordForConfirm by centerPasswordForConfirm.collectAsStateWithLifecycle()

            CenterSignUpScreen(
                signUpProcess = signUpProcess,
                centerName = centerName,
                centerPhoneNumber = centerPhoneNumber,
                centerAuthCodeTimerMinute = centerAuthCodeTimerMinute,
                centerAuthCodeTimerSeconds = centerAuthCodeTimerSeconds,
                centerAuthCode = centerAuthCode,
                isConfirmAuthCode = isConfirmAuthCode,
                businessRegistrationNumber = businessRegistrationNumber,
                businessRegistrationInfo = businessRegistrationInfo,
                centerId = centerId,
                centerIdResult = centerIdResult,
                centerPassword = centerPassword,
                centerPasswordForConfirm = centerPasswordForConfirm,
                setSignUpProcess = ::setCenterSignUpProcess,
                onCenterNameChanged = ::setCenterName,
                onCenterPhoneNumberChanged = ::setCenterPhoneNumber,
                onCenterAuthCodeChanged = ::setCenterAuthCode,
                onBusinessRegistrationNumberChanged = ::setBusinessRegistrationNumber,
                onCenterIdChanged = ::setCenterId,
                onCenterPasswordChanged = ::setCenterPassword,
                onCenterPasswordForConfirmChanged = ::setCenterPasswordForConfirm,
                sendPhoneNumber = ::sendPhoneNumber,
                confirmAuthCode = ::confirmAuthCode,
                signUpCenter = ::signUpCenter,
                validateIdentifier = ::validateIdentifier,
                validateBusinessRegistrationNumber = ::validateBusinessRegistrationNumber,
            )
        }
    }

    private fun handleEvent(event: CenterSignUpEvent) = when (event) {
        is CenterSignUpEvent.NavigateTo -> findNavController()
            .deepLinkNavigateTo(requireContext(), event.destination)
    }
}


@Composable
internal fun CenterSignUpScreen(
    signUpProcess: CenterSignUpProcess,
    centerName: String,
    centerPhoneNumber: String,
    centerAuthCodeTimerMinute: String,
    centerAuthCodeTimerSeconds: String,
    centerAuthCode: String,
    isConfirmAuthCode: Boolean,
    businessRegistrationNumber: String,
    businessRegistrationInfo: BusinessRegistrationInfo?,
    centerId: String,
    centerIdResult: Boolean,
    centerPassword: String,
    centerPasswordForConfirm: String,
    setSignUpProcess: (CenterSignUpProcess) -> Unit,
    onCenterNameChanged: (String) -> Unit,
    onCenterPhoneNumberChanged: (String) -> Unit,
    onCenterAuthCodeChanged: (String) -> Unit,
    onBusinessRegistrationNumberChanged: (String) -> Unit,
    onCenterIdChanged: (String) -> Unit,
    onCenterPasswordChanged: (String) -> Unit,
    onCenterPasswordForConfirmChanged: (String) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
    signUpCenter: () -> Unit,
    validateIdentifier: () -> Unit,
    validateBusinessRegistrationNumber: () -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current
    val (businessRegistrationProcessed, setBusinessRegistrationProcessed)
            = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CareTopAppBar(
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
                currentStep = signUpProcess.step,
                totalSteps = CenterSignUpProcess.entries.size,
                modifier = Modifier.fillMaxWidth(),
            )

            AnimatedContent(
                targetState = signUpProcess,
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
            ) { signUpProcess ->
                when (signUpProcess) {
                    CenterSignUpProcess.NAME ->
                        CenterNameScreen(
                            centerName = centerName,
                            onCenterNameChanged = onCenterNameChanged,
                            setSignUpProcess = setSignUpProcess,
                        )

                    CenterSignUpProcess.PHONE_NUMBER ->
                        CenterPhoneNumberScreen(
                            centerPhoneNumber = centerPhoneNumber,
                            centerAuthCodeTimerMinute = centerAuthCodeTimerMinute,
                            centerAuthCodeTimerSeconds = centerAuthCodeTimerSeconds,
                            centerAuthCode = centerAuthCode,
                            isConfirmAuthCode = isConfirmAuthCode,
                            businessRegistrationProcessed = businessRegistrationProcessed,
                            onCenterPhoneNumberChanged = onCenterPhoneNumberChanged,
                            onCenterAuthCodeChanged = onCenterAuthCodeChanged,
                            setSignUpProcess = setSignUpProcess,
                            sendPhoneNumber = sendPhoneNumber,
                            confirmAuthCode = confirmAuthCode,
                            setBusinessRegistrationProcessed = setBusinessRegistrationProcessed,
                        )

                    CenterSignUpProcess.BUSINESS_REGISTRATION_NUMBER ->
                        BusinessRegistrationScreen(
                            businessRegistrationNumber = businessRegistrationNumber,
                            businessRegistrationInfo = businessRegistrationInfo,
                            onBusinessRegistrationNumberChanged = onBusinessRegistrationNumberChanged,
                            validateBusinessRegistrationNumber = validateBusinessRegistrationNumber,
                            setSignUpProcess = setSignUpProcess,
                        )

                    CenterSignUpProcess.ID_PASSWORD ->
                        IdPasswordScreen(
                            centerId = centerId,
                            centerIdResult = centerIdResult,
                            centerPassword = centerPassword,
                            centerPasswordForConfirm = centerPasswordForConfirm,
                            onCenterIdChanged = onCenterIdChanged,
                            onCenterPasswordChanged = onCenterPasswordChanged,
                            onCenterPasswordForConfirmChanged = onCenterPasswordForConfirmChanged,
                            setSignUpProcess = setSignUpProcess,
                            validateIdentifier = validateIdentifier,
                            signUpCenter = signUpCenter,
                        )
                }
            }
        }
    }
}