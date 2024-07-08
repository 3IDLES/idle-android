package com.idle.signin.center.newpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.idle.binding.deepLinkNavigateTo
import com.idle.binding.repeatOnStarted
import com.idle.signin.center.newpassword.process.GenerateNewPasswordScreen
import com.idle.signin.center.newpassword.process.PhoneNumberScreen

class NewPasswordFragment : Fragment() {
    private lateinit var composeView: ComposeView
    private val viewModel: NewPasswordViewModel by viewModels()

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
                val phoneNumber by phoneNumber.collectAsStateWithLifecycle()
                val certificationNumber by certificationNumber.collectAsStateWithLifecycle()
                val generateNewPasswordProcess by generateNewPasswordProcess.collectAsStateWithLifecycle()
                val newPassword by newPassword.collectAsStateWithLifecycle()
                val newPasswordForConfirm by newPasswordForConfirm.collectAsStateWithLifecycle()

                NewPasswordScreen(
                    generateNewPasswordProcess = generateNewPasswordProcess,
                    phoneNumber = phoneNumber,
                    certificationNumber = certificationNumber,
                    newPassword = newPassword,
                    newPasswordForConfirm = newPasswordForConfirm,
                    setGenerateNewPasswordProcess = ::setGenerateNewPasswordProcess,
                    onPhoneNumberChanged = ::setPhoneNumber,
                    onCertificationNumberChanged = ::setCertificationNumber,
                    onNewPasswordChanged = ::setNewPassword,
                    onNewPasswordForConfirmChanged = ::setNewPasswordForConfirm,
                )
            }
        }
    }

    private fun handleEvent(event: NewPasswordEvent) = when (event) {
        is NewPasswordEvent.NavigateTo -> findNavController()
            .deepLinkNavigateTo(requireContext(), event.destination)
    }
}

@Composable
internal fun NewPasswordScreen(
    phoneNumber: String,
    certificationNumber: String,
    newPassword: String,
    newPasswordForConfirm: String,
    generateNewPasswordProcess: GenerateNewPasswordProcess,
    onPhoneNumberChanged: (String) -> Unit,
    onCertificationNumberChanged: (String) -> Unit,
    onNewPasswordChanged: (String) -> Unit,
    onNewPasswordForConfirmChanged: (String) -> Unit,
    setGenerateNewPasswordProcess: (GenerateNewPasswordProcess) -> Unit,
) {
    when (generateNewPasswordProcess) {
        GenerateNewPasswordProcess.PHONE_NUMBER -> PhoneNumberScreen(
            phoneNumber = phoneNumber,
            certificationNumber = certificationNumber,
            onPhoneNumberChanged = onPhoneNumberChanged,
            onCertificationNumberChanged = onCertificationNumberChanged,
            setGenerateNewPasswordProcess = setGenerateNewPasswordProcess,
        )

        GenerateNewPasswordProcess.GENERATE_NEW_PASSWORD -> GenerateNewPasswordScreen(
            newPassword = newPassword,
            newPasswordForConfirm = newPasswordForConfirm,
            onNewPasswordChanged = onNewPasswordChanged,
            onNewPasswordForConfirmChanged = onNewPasswordForConfirmChanged,
            setGenerateNewPasswordProcess = setGenerateNewPasswordProcess,
        )
    }
}