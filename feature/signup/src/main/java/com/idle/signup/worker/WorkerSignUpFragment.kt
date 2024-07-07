package com.idle.signin.worker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.idle.common_ui.deepLinkNavigateTo
import com.idle.common_ui.repeatOnStarted
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
                    sendAuthNumber = ::sendAuthNumber,
                    confirmAuthNumber = ::confirmAuthNumber,
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
    sendAuthNumber: () -> Unit,
    confirmAuthNumber: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
    ) {
        when (signUpProcess) {
            WorkerSignUpProcess.NAME -> WorkerNameScreen(
                workerName = workerName,
                onWorkerNameChanged = onWorkerNameChanged,
                setSignUpProcess = setSignUpProcess
            )

            WorkerSignUpProcess.PHONE_NUMBER -> WorkerPhoneNumberScreen(
                workerPhoneNumber = workerPhoneNumber,
                workerCertificationNumber = workerCertificateNumber,
                onWorkerPhoneNumberChanged = onWorkerPhoneNumberChanged,
                onWorkerCertificationNumberChanged = onWorkerCertificateNumberChanged,
                setSignUpProcess = setSignUpProcess,
                sendAuthNumber = sendAuthNumber,
                confirmAuthNumber = confirmAuthNumber,
            )

            WorkerSignUpProcess.GENDER -> GenderScreen(
                gender = gender,
                onGenderChanged = onGenderChanged,
                setSignUpProcess = setSignUpProcess
            )
        }
    }
}