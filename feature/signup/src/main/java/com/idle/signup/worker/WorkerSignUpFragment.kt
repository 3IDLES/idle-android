package com.idle.signin.worker

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
import com.idle.designsystem.compose.component.CareTopAppBar
import com.idle.signin.center.CenterSignUpProcess
import com.idle.signup.worker.process.AddressScreen
import com.idle.signup.worker.process.GenderScreen
import com.idle.signup.worker.process.WorkerNameScreen
import com.idle.signup.worker.process.WorkerPhoneNumberScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerSignUpFragment : BaseComposeFragment() {
    override val viewModel: WorkerSignUpViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        viewModel.apply {
            val signUpProcess by signUpProcess.collectAsStateWithLifecycle()
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
                signUpProcess = signUpProcess,
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
                setSignUpProcess = ::setWorkerSignUpProcess,
                sendPhoneNumber = ::sendPhoneNumber,
                confirmAuthCode = ::confirmAuthCode,
            )
        }
    }
}


@Composable
internal fun WorkerSignUpScreen(
    signUpProcess: WorkerSignUpProcess,
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
    setSignUpProcess: (WorkerSignUpProcess) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current
    val (phoneNumberProcessed, setPhoneNumberProcessed) = remember { mutableStateOf(false) }
    val (addressProcessed, setAddressProcessed) = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CareTopAppBar(
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
                label = "요양 보호사의 회원가입을 관리하는 애니메이션",
            ) { signUpProcess ->
                when (signUpProcess) {
                    WorkerSignUpProcess.NAME -> WorkerNameScreen(
                        workerName = workerName,
                        onWorkerNameChanged = onWorkerNameChanged,
                        setSignUpProcess = setSignUpProcess
                    )

                    WorkerSignUpProcess.GENDER -> GenderScreen(
                        gender = gender,
                        phoneNumberProcessed = phoneNumberProcessed,
                        onGenderChanged = onGenderChanged,
                        setSignUpProcess = setSignUpProcess,
                        setPhoneNumberProcessed = setPhoneNumberProcessed,
                    )

                    WorkerSignUpProcess.PHONE_NUMBER -> WorkerPhoneNumberScreen(
                        workerPhoneNumber = workerPhoneNumber,
                        workerAuthCodeTimerMinute = workerAuthCodeTimerMinute,
                        workerAuthCodeTimerSeconds = workerAuthCodeTimerSeconds,
                        workerAuthCode = workerAuthCode,
                        isConfirmAuthCode = isConfirmAuthCode,
                        addressProcessed = addressProcessed,
                        onWorkerPhoneNumberChanged = onWorkerPhoneNumberChanged,
                        onWorkerAuthCodeChanged = onWorkerAuthCodeChanged,
                        setSignUpProcess = setSignUpProcess,
                        sendPhoneNumber = sendPhoneNumber,
                        confirmAuthCode = confirmAuthCode,
                        setAddressProcessed = setAddressProcessed,
                    )

                    WorkerSignUpProcess.ADDRESS -> AddressScreen(
                        address = address,
                        addressDetail = addressDetail,
                        onAddressChanged = onAddressChanged,
                        onAddressDetailChanged = onAddressDetailChanged,
                        setSignUpProcess = setSignUpProcess,
                    )
                }
            }
        }
    }
}