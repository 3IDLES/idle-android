package com.idle.auth.center

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.idle.auth.center.WorkerAuthEvent.NavigateTo
import com.idle.binding.DeepLinkDestination.WorkerSignUp
import com.idle.binding.deepLinkNavigateTo
import com.idle.binding.repeatOnStarted
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.designsystem.compose.foundation.PretendardMedium
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
                navigateToWorkerSignUp = { viewModel.event(NavigateTo(WorkerSignUp)) }
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
    navigateToWorkerSignUp: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 30.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "요양 보호사님, 환영합니다!",
            style = CareTheme.typography.heading1,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Spacer(
            modifier = Modifier.size(120.dp)
                .background(CareTheme.colors.gray900),
        )

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "휴대폰 번호로 시작하기",
            onClick = navigateToWorkerSignUp,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}