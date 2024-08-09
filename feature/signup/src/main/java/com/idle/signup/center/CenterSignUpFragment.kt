package com.idle.signin.center

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.domain.model.auth.BusinessRegistrationInfo
import com.idle.designresource.R
import com.idle.signup.center.step.BusinessRegistrationScreen
import com.idle.signup.center.step.CenterNameScreen
import com.idle.signup.center.step.CenterPhoneNumberScreen
import com.idle.signup.center.step.IdPasswordScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterSignUpFragment : BaseComposeFragment() {
    override val fragmentViewModel: CenterSignUpViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {

            val signUpStep by signUpStep.collectAsStateWithLifecycle()
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
                signUpStep = signUpStep,
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
                setSignUpStep = ::setCenterSignUpStep,
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
}


@Composable
internal fun CenterSignUpScreen(
    signUpStep: CenterSignUpStep,
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
    setSignUpStep: (CenterSignUpStep) -> Unit,
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
            CareSubtitleTopBar(
                title = stringResource(id = R.string.center_signup),
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, bottom = 8.dp)
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
                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp),
        ) {
            CareProgressBar(
                currentStep = signUpStep.step,
                totalSteps = CenterSignUpStep.entries.size,
                modifier = Modifier.fillMaxWidth(),
            )

            CareStateAnimator(
                targetState = signUpStep,
                label = stringResource(id = R.string.center_info_input_animation_label),
            ) { signUpStep ->
                when (signUpStep) {
                    CenterSignUpStep.NAME ->
                        CenterNameScreen(
                            centerName = centerName,
                            onCenterNameChanged = onCenterNameChanged,
                            setSignUpStep = setSignUpStep,
                        )

                    CenterSignUpStep.PHONE_NUMBER ->
                        CenterPhoneNumberScreen(
                            centerPhoneNumber = centerPhoneNumber,
                            centerAuthCodeTimerMinute = centerAuthCodeTimerMinute,
                            centerAuthCodeTimerSeconds = centerAuthCodeTimerSeconds,
                            centerAuthCode = centerAuthCode,
                            isConfirmAuthCode = isConfirmAuthCode,
                            businessRegistrationProcessed = businessRegistrationProcessed,
                            onCenterPhoneNumberChanged = onCenterPhoneNumberChanged,
                            onCenterAuthCodeChanged = onCenterAuthCodeChanged,
                            setSignUpStep = setSignUpStep,
                            sendPhoneNumber = sendPhoneNumber,
                            confirmAuthCode = confirmAuthCode,
                            setBusinessRegistrationProcessed = setBusinessRegistrationProcessed,
                        )

                    CenterSignUpStep.BUSINESS_REGISTRATION ->
                        BusinessRegistrationScreen(
                            businessRegistrationNumber = businessRegistrationNumber,
                            businessRegistrationInfo = businessRegistrationInfo,
                            onBusinessRegistrationNumberChanged = onBusinessRegistrationNumberChanged,
                            validateBusinessRegistrationNumber = validateBusinessRegistrationNumber,
                            setSignUpStep = setSignUpStep,
                        )

                    CenterSignUpStep.ID_PASSWORD ->
                        IdPasswordScreen(
                            centerId = centerId,
                            centerIdResult = centerIdResult,
                            centerPassword = centerPassword,
                            centerPasswordForConfirm = centerPasswordForConfirm,
                            onCenterIdChanged = onCenterIdChanged,
                            onCenterPasswordChanged = onCenterPasswordChanged,
                            onCenterPasswordForConfirmChanged = onCenterPasswordForConfirmChanged,
                            setSignUpStep = setSignUpStep,
                            validateIdentifier = validateIdentifier,
                            signUpCenter = signUpCenter,
                        )
                }
            }
        }
    }
}