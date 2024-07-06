package com.idle.signin.center

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
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
import com.idle.signup.center.process.IdPasswordScreen
import com.idle.signup.center.process.NameScreen
import com.idle.signup.center.process.PhoneNumberScreen
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
            val signUpProcess by viewModel.signUpProcess.collectAsStateWithLifecycle()
            val centerName by viewModel.centerName.collectAsStateWithLifecycle()

            CenterSignUpScreen(
                signUpProcess = signUpProcess,
                centerName = centerName,
                onCenterNameChanged = viewModel::setCenterName,
                setSignUpProcess = viewModel::setCenterSignUpProcess,
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
    onCenterNameChanged: (String) -> Unit,
    setSignUpProcess: (CenterSignUpProcess) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
    ) {
        when (signUpProcess) {
            CenterSignUpProcess.NAME -> NameScreen(
                centerName = centerName,
                onCenterNameChanged = onCenterNameChanged,
                setSignUpProcess = setSignUpProcess
            )

            CenterSignUpProcess.PHONE_NUMBER ->
                PhoneNumberScreen(setSignUpProcess = setSignUpProcess)

            CenterSignUpProcess.BUSINESS_REGISTRAION_NUMBER ->
                BusinessRegistrationScreen(setSignUpProcess = setSignUpProcess)

            CenterSignUpProcess.ID_PASSWORD -> IdPasswordScreen(setSignUpProcess = setSignUpProcess)
        }
    }
}