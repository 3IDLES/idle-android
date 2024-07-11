package com.idle.signin.worker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.idle.binding.deepLinkNavigateTo
import com.idle.binding.repeatOnStarted
import com.idle.compose.addFocusCleaner
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareTopAppBar
import com.idle.signin.center.CenterSignUpProcess
import com.idle.signup.worker.process.AddressScreen
import com.idle.signup.worker.process.GenderScreen
import com.idle.signup.worker.process.WorkerNameScreen
import com.idle.signup.worker.process.WorkerPhoneNumberScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerSignUpFragment : Fragment() {

    private lateinit var composeView: ComposeView
    private val viewModel: WorkerSignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect { handleEvent(it) }
        }

        composeView.setContent {
            viewModel.apply {
                val signUpProcess by signUpProcess.collectAsStateWithLifecycle()
                val workerName by workerName.collectAsStateWithLifecycle()
                val workerPhoneNumber by workerPhoneNumber.collectAsStateWithLifecycle()
                val workerCertificateNumber by workerConfirmNumber.collectAsStateWithLifecycle()
                val gender by gender.collectAsStateWithLifecycle()

                WorkerSignUpScreen(
                    signUpProcess = signUpProcess,
                    workerName = workerName,
                    workerPhoneNumber = workerPhoneNumber,
                    workerCertificateNumber = workerCertificateNumber,
                    gender = gender,
                    onWorkerNameChanged = ::setWorkerName,
                    onWorkerPhoneNumberChanged = ::setWorkerPhoneNumber,
                    onWorkerCertificateNumberChanged = ::setWorkerCertificateNumber,
                    onGenderChanged = ::setGender,
                    setSignUpProcess = ::setWorkerSignUpProcess,
                    sendPhoneNumber = ::sendPhoneNumber,
                    confirmAuthCode = ::confirmAuthCode,
                )
            }
        }
    }

    private fun handleEvent(event: WorkerSignUpEvent) = when (event) {
        is WorkerSignUpEvent.NavigateTo -> findNavController()
            .deepLinkNavigateTo(requireContext(), event.destination)
    }
}


@Composable
internal fun WorkerSignUpScreen(
    signUpProcess: WorkerSignUpProcess,
    workerName: String,
    workerPhoneNumber: String,
    workerCertificateNumber: String,
    gender: Gender,
    onWorkerNameChanged: (String) -> Unit,
    onWorkerPhoneNumberChanged: (String) -> Unit,
    onWorkerCertificateNumberChanged: (String) -> Unit,
    onGenderChanged: (Gender) -> Unit,
    setSignUpProcess: (WorkerSignUpProcess) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current

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

            when (signUpProcess) {
                WorkerSignUpProcess.NAME -> WorkerNameScreen(
                    workerName = workerName,
                    onWorkerNameChanged = onWorkerNameChanged,
                    setSignUpProcess = setSignUpProcess
                )

                WorkerSignUpProcess.GENDER -> GenderScreen(
                    gender = gender,
                    onGenderChanged = onGenderChanged,
                    setSignUpProcess = setSignUpProcess
                )

                WorkerSignUpProcess.PHONE_NUMBER -> WorkerPhoneNumberScreen(
                    workerPhoneNumber = workerPhoneNumber,
                    workerCertificationNumber = workerCertificateNumber,
                    onWorkerPhoneNumberChanged = onWorkerPhoneNumberChanged,
                    onWorkerCertificationNumberChanged = onWorkerCertificateNumberChanged,
                    setSignUpProcess = setSignUpProcess,
                    sendPhoneNumber = sendPhoneNumber,
                    confirmAuthCode = confirmAuthCode,
                )

                WorkerSignUpProcess.ADDRESS -> AddressScreen(
                    onAddressDetailChanged = {},
                    setSignUpProcess = setSignUpProcess,
                )
            }
        }
    }
}