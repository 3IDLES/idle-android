package com.idle.auth.center

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.idle.auth.center.WorkerAuthEvent.NavigateTo
import com.idle.common_ui.DeepLinkDestination.WorkerSignIn
import com.idle.common_ui.deepLinkNavigateTo
import com.idle.common_ui.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerAuthFragment : Fragment() {

    private lateinit var composeView: ComposeView
    private val viewModel: WorkerAuthViewModel by viewModels()

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
            WorkerAuthScreen(
                navigateToWorkerSignIn = { viewModel.event(NavigateTo(WorkerSignIn)) }
            )
        }
    }

    private fun handleEvent(event: WorkerAuthEvent) = when (event) {
        is NavigateTo -> findNavController()
            .deepLinkNavigateTo(requireContext(), event.destination)
    }
}


@Composable
internal fun WorkerAuthScreen(
    navigateToWorkerSignIn: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
    ) {
        Text(text = "요양 보호사님, 환영합니다!")

        Text(text = "기타 환영 멘트")

        Button(onClick = navigateToWorkerSignIn) {
            Text(text = "휴대폰 번호로 시작하기")
        }
    }
}