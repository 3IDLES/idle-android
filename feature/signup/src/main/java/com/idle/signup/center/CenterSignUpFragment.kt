package com.idle.signin.center

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
import com.idle.signup.center.process.BusinessRegistrationScreen
import com.idle.signup.center.process.CenterNameScreen
import com.idle.signup.center.process.CenterPhoneNumberScreen
import com.idle.signup.center.process.IdPasswordScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterSignUpFragment : Fragment() {

    private lateinit var composeView: ComposeView
    private val viewModel: CenterSignUpViewModel by viewModels()

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
                val centerName by centerName.collectAsStateWithLifecycle()
                val centerPhoneNumber by centerPhoneNumber.collectAsStateWithLifecycle()
                val centerCertificateNumber by centerCertificateNumber.collectAsStateWithLifecycle()
                val businessRegistrationNumber
                        by businessRegistrationNumber.collectAsStateWithLifecycle()
                val centerId by centerId.collectAsStateWithLifecycle()
                val centerPassword by centerPassword.collectAsStateWithLifecycle()
                val centerPasswordForConfirm
                        by centerPasswordForConfirm.collectAsStateWithLifecycle()

                CenterSignUpScreen(
                    signUpProcess = signUpProcess,
                    centerName = centerName,
                    centerPhoneNumber = centerPhoneNumber,
                    centerCertificateNumber = centerCertificateNumber,
                    businessRegistrationNumber = businessRegistrationNumber,
                    centerId = centerId,
                    centerPassword = centerPassword,
                    centerPasswordForConfirm = centerPasswordForConfirm,
                    setSignUpProcess = ::setCenterSignUpProcess,
                    onCenterNameChanged = ::setCenterName,
                    onCenterPhoneNumberChanged = ::setCenterPhoneNumber,
                    onCenterCertificateNumberChanged = ::setCenterCertificateNumber,
                    onBusinessRegistrationNumberChanged = ::setBusinessRegistrationNumber,
                    onCenterIdChanged = ::setCenterId,
                    onCenterPasswordChanged = ::setCenterPassword,
                    onCenterPasswordForConfirmChanged = ::setCenterPasswordForConfirm,
                )
            }
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
    centerCertificateNumber: String,
    businessRegistrationNumber: String,
    centerId: String,
    centerPassword: String,
    centerPasswordForConfirm: String,
    setSignUpProcess: (CenterSignUpProcess) -> Unit,
    onCenterNameChanged: (String) -> Unit,
    onCenterPhoneNumberChanged: (String) -> Unit,
    onCenterCertificateNumberChanged: (String) -> Unit,
    onBusinessRegistrationNumberChanged: (String) -> Unit,
    onCenterIdChanged: (String) -> Unit,
    onCenterPasswordChanged: (String) -> Unit,
    onCenterPasswordForConfirmChanged: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
    ) {
        when (signUpProcess) {
            CenterSignUpProcess.NAME -> CenterNameScreen(
                centerName = centerName,
                onCenterNameChanged = onCenterNameChanged,
                setSignUpProcess = setSignUpProcess
            )

            CenterSignUpProcess.PHONE_NUMBER ->
                CenterPhoneNumberScreen(
                    centerPhoneNumber = centerPhoneNumber,
                    centerCertificationNumber = centerCertificateNumber,
                    onCenterPhoneNumberChanged = onCenterPhoneNumberChanged,
                    onCenterCertificationNumberChanged = onCenterCertificateNumberChanged,
                    setSignUpProcess = setSignUpProcess
                )

            CenterSignUpProcess.BUSINESS_REGISTRAION_NUMBER ->
                BusinessRegistrationScreen(
                    businessRegistrationNumber = businessRegistrationNumber,
                    onBusinessRegistrationNumberChanged = onBusinessRegistrationNumberChanged,
                    setSignUpProcess = setSignUpProcess
                )

            CenterSignUpProcess.ID_PASSWORD -> IdPasswordScreen(
                centerId = centerId,
                centerPassword = centerPassword,
                centerPasswordForConfirm = centerPasswordForConfirm,
                onCenterIdChanged = onCenterIdChanged,
                onCenterPasswordChanged = onCenterPasswordChanged,
                onCenterPasswordForConfirmChanged = onCenterPasswordForConfirmChanged,
                setSignUpProcess = setSignUpProcess
            )
        }
    }
}