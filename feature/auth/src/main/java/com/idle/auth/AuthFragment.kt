package com.idle.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.idle.binding.DeepLinkDestination.CenterAuth
import com.idle.binding.DeepLinkDestination.WorkerAuth
import com.idle.binding.deepLinkNavigateTo
import com.idle.binding.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class AuthFragment : Fragment() {

    private lateinit var composeView: ComposeView
    private val viewModel: AuthViewModel by viewModels()

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
            AuthScreen(
                navigateToWorkerAuth = {
                    viewModel.event(AuthEvent.NavigateTo(WorkerAuth))
                },
                navigateToCenterAuth = {
                    viewModel.event(AuthEvent.NavigateTo(CenterAuth))
                },
            )
        }
    }

    private fun handleEvent(event: AuthEvent) = when (event) {
        is AuthEvent.NavigateTo -> findNavController()
            .deepLinkNavigateTo(requireContext(), event.destination)
    }
}


@Composable
internal fun AuthScreen(
    navigateToWorkerAuth: () -> Unit = {},
    navigateToCenterAuth: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
    ) {
        Spacer(
            modifier = Modifier.height(300.dp)
                .fillMaxWidth()
                .background(Color.Black)
        )

        Button(onClick = navigateToWorkerAuth) {
            Text(text = "요양 보호사로 시작하기")
        }

        Button(onClick = navigateToCenterAuth) {
            Text(text = "센터 관리자로 시작하기")
        }
    }
}