package com.idle.signin.worker

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.idle.common_ui.deepLinkNavigateTo
import com.idle.common_ui.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerSignInFragment : Fragment() {

    private lateinit var composeView: ComposeView
    private val viewModel: WorkerSignInViewModel by viewModels()

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
            WorkerSignInScreen()
        }
    }

    private fun handleEvent(event: WorkerSignInEvent) = when (event) {
        is WorkerSignInEvent.NavigateTo -> findNavController()
            .deepLinkNavigateTo(requireContext(), event.destination)
    }
}


@Composable
internal fun WorkerSignInScreen() {
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
    }
}