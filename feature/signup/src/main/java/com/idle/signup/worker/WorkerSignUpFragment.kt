package com.idle.signin.worker

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
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopAppBar
import com.idle.domain.model.auth.Gender
import com.idle.signin.center.CenterSignUpStep
import com.idle.signup.worker.step.AddressScreen
import com.idle.signup.worker.step.GenderScreen
import com.idle.signup.worker.step.WorkerNameScreen
import com.idle.signup.worker.step.WorkerPhoneNumberScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerSignUpFragment : BaseComposeFragment() {
    override val fragmentViewModel: WorkerSignUpViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val signUpStep by signUpStep.collectAsStateWithLifecycle()
            val workerName by workerName.collectAsStateWithLifecycle()
            val workerPhoneNumber by workerPhoneNumber.collectAsStateWithLifecycle()
            val workerAuthCodeTimerMinute by workerAuthCodeTimerMinute.collectAsStateWithLifecycle()
            val workerAuthCodeTimerSeconds by workerAuthCodeTimerSeconds.collectAsStateWithLifecycle()
            val workerAuthCode by workerAuthCode.collectAsStateWithLifecycle()
            val isConfirmAuthCode by isConfirmAuthCode.collectAsStateWithLifecycle()
            val gender by gender.collectAsStateWithLifecycle()
            val address by address.collectAsStateWithLifecycle()
            val addressDetail by addressDetail.collectAsStateWithLifecycle()

            WorkerSignUpScreen(
                signUpStep = signUpStep,
                workerName = workerName,
                workerPhoneNumber = workerPhoneNumber,
                workerAuthCodeTimerMinute = workerAuthCodeTimerMinute,
                workerAuthCodeTimerSeconds = workerAuthCodeTimerSeconds,
                workerAuthCode = workerAuthCode,
                isConfirmAuthCode = isConfirmAuthCode,
                gender = gender,
                address = address,
                addressDetail = addressDetail,
                onWorkerNameChanged = ::setWorkerName,
                onWorkerPhoneNumberChanged = ::setWorkerPhoneNumber,
                onWorkerAuthCodeChanged = ::setWorkerAuthCode,
                onGenderChanged = ::setGender,
                onAddressChanged = ::setAddress,
                onAddressDetailChanged = ::setAddressDetail,
                setSignUpStep = ::setWorkerSignUpStep,
                sendPhoneNumber = ::sendPhoneNumber,
                confirmAuthCode = ::confirmAuthCode,
                signUpWorker = ::signUpWorker,
            )
        }
    }
}


@Composable
internal fun WorkerSignUpScreen(
    signUpStep: WorkerSignUpStep,
    workerName: String,
    workerPhoneNumber: String,
    workerAuthCodeTimerMinute: String,
    workerAuthCodeTimerSeconds: String,
    workerAuthCode: String,
    isConfirmAuthCode: Boolean,
    gender: Gender,
    address: String,
    addressDetail: String,
    onWorkerNameChanged: (String) -> Unit,
    onWorkerPhoneNumberChanged: (String) -> Unit,
    onWorkerAuthCodeChanged: (String) -> Unit,
    onGenderChanged: (Gender) -> Unit,
    onAddressChanged: (String) -> Unit,
    onAddressDetailChanged: (String) -> Unit,
    setSignUpStep: (WorkerSignUpStep) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
    signUpWorker: () -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current
    val (phoneNumberProcessed, setPhoneNumberProcessed) = remember { mutableStateOf(false) }
    val (addressProcessed, setAddressProcessed) = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CareSubtitleTopAppBar(
                title = "요양보호사 회원가입",
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
                currentStep = signUpStep.step,
                totalSteps = CenterSignUpStep.entries.size,
                modifier = Modifier.fillMaxWidth(),
            )

            CareStateAnimator(
                targetState = signUpStep,
                label = "요양 보호사의 회원가입을 관리하는 애니메이션",
            ) { signUpStep ->
                when (signUpStep) {
                    WorkerSignUpStep.NAME -> WorkerNameScreen(
                        workerName = workerName,
                        onWorkerNameChanged = onWorkerNameChanged,
                        setSignUpStep = setSignUpStep
                    )

                    WorkerSignUpStep.GENDER -> GenderScreen(
                        gender = gender,
                        phoneNumberProcessed = phoneNumberProcessed,
                        onGenderChanged = onGenderChanged,
                        setSignUpStep = setSignUpStep,
                        setPhoneNumberProcessed = setPhoneNumberProcessed,
                    )

                    WorkerSignUpStep.PHONE_NUMBER -> WorkerPhoneNumberScreen(
                        workerPhoneNumber = workerPhoneNumber,
                        workerAuthCodeTimerMinute = workerAuthCodeTimerMinute,
                        workerAuthCodeTimerSeconds = workerAuthCodeTimerSeconds,
                        workerAuthCode = workerAuthCode,
                        isConfirmAuthCode = isConfirmAuthCode,
                        addressProcessed = addressProcessed,
                        onWorkerPhoneNumberChanged = onWorkerPhoneNumberChanged,
                        onWorkerAuthCodeChanged = onWorkerAuthCodeChanged,
                        setSignUpStep = setSignUpStep,
                        sendPhoneNumber = sendPhoneNumber,
                        confirmAuthCode = confirmAuthCode,
                        setAddressProcessed = setAddressProcessed,
                    )

                    WorkerSignUpStep.ADDRESS -> AddressScreen(
                        address = address,
                        addressDetail = addressDetail,
                        onAddressChanged = onAddressChanged,
                        onAddressDetailChanged = onAddressDetailChanged,
                        setSignUpStep = setSignUpStep,
                        signUpWorker = signUpWorker,
                    )
                }
            }
        }
    }
}