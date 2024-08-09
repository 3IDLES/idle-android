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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.domain.model.auth.Gender
import com.idle.post.code.PostCodeFragment
import com.idle.signup.worker.step.AddressScreen
import com.idle.signup.worker.step.WorkerInformationScreen
import com.idle.signup.worker.step.WorkerPhoneNumberScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerSignUpFragment : BaseComposeFragment() {
    override val fragmentViewModel: WorkerSignUpViewModel by viewModels()

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
            val signUpStep by signUpStep.collectAsStateWithLifecycle()
            val workerPhoneNumber by workerPhoneNumber.collectAsStateWithLifecycle()
            val workerAuthCodeTimerMinute by workerAuthCodeTimerMinute.collectAsStateWithLifecycle()
            val workerAuthCodeTimerSeconds by workerAuthCodeTimerSeconds.collectAsStateWithLifecycle()
            val workerAuthCode by workerAuthCode.collectAsStateWithLifecycle()
            val isConfirmAuthCode by isConfirmAuthCode.collectAsStateWithLifecycle()
            val workerName by workerName.collectAsStateWithLifecycle()
            val birthYear by birthYear.collectAsStateWithLifecycle()
            val gender by gender.collectAsStateWithLifecycle()
            val roadNameAddress by roadNameAddress.collectAsStateWithLifecycle()
            val addressDetail by addressDetail.collectAsStateWithLifecycle()

            WorkerSignUpScreen(
                signUpStep = signUpStep,
                workerPhoneNumber = workerPhoneNumber,
                workerAuthCodeTimerMinute = workerAuthCodeTimerMinute,
                workerAuthCodeTimerSeconds = workerAuthCodeTimerSeconds,
                workerAuthCode = workerAuthCode,
                isConfirmAuthCode = isConfirmAuthCode,
                workerName = workerName,
                birthYear = birthYear,
                gender = gender,
                roadNameAddress = roadNameAddress,
                addressDetail = addressDetail,
                onWorkerPhoneNumberChanged = ::setWorkerPhoneNumber,
                onWorkerAuthCodeChanged = ::setWorkerAuthCode,
                onWorkerNameChanged = ::setWorkerName,
                onBirthYearChanged = ::setBirthYear,
                onGenderChanged = ::setGender,
                showPostCodeDialog = {
                    if (!(postCodeDialog?.isAdded == true || postCodeDialog?.isVisible == true)) {
                        postCodeDialog?.show(parentFragmentManager, "PostCodeFragment")
                    }
                },
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
    workerPhoneNumber: String,
    workerAuthCodeTimerMinute: String,
    workerAuthCodeTimerSeconds: String,
    workerAuthCode: String,
    isConfirmAuthCode: Boolean,
    workerName: String,
    birthYear: String,
    gender: Gender,
    roadNameAddress: String,
    addressDetail: String,
    onWorkerPhoneNumberChanged: (String) -> Unit,
    onWorkerAuthCodeChanged: (String) -> Unit,
    onWorkerNameChanged: (String) -> Unit,
    onBirthYearChanged: (String) -> Unit,
    onGenderChanged: (Gender) -> Unit,
    showPostCodeDialog: () -> Unit,
    onAddressDetailChanged: (String) -> Unit,
    setSignUpStep: (WorkerSignUpStep) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
    signUpWorker: () -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current
    val (addressProcessed, setAddressProcessed) = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(start = 12.dp, top = 48.dp, end = 20.dp)) {
                CareSubtitleTopBar(
                    title = stringResource(id = R.string.worker_signup),
                    onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                    modifier = Modifier.fillMaxWidth(),
                )

                CareProgressBar(
                    currentStep = signUpStep.step,
                    totalSteps = WorkerSignUpStep.entries.size,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                )
            }
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
                .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 28.dp),
        ) {
            CareStateAnimator(
                targetState = signUpStep,
                label = "요양 보호사의 회원가입을 관리하는 애니메이션",
            ) { signUpStep ->
                when (signUpStep) {
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

                    WorkerSignUpStep.INFO -> WorkerInformationScreen(
                        workerName = workerName,
                        birthYear = birthYear,
                        gender = gender,
                        onWorkerNameChanged = onWorkerNameChanged,
                        onBirthYearChanged = onBirthYearChanged,
                        onGenderChanged = onGenderChanged,
                        setSignUpStep = setSignUpStep
                    )

                    WorkerSignUpStep.ADDRESS -> AddressScreen(
                        roadNameAddress = roadNameAddress,
                        addressDetail = addressDetail,
                        showPostCode = showPostCodeDialog,
                        onAddressDetailChanged = onAddressDetailChanged,
                        setSignUpStep = setSignUpStep,
                        signUpWorker = signUpWorker,
                    )
                }
            }
        }
    }
}