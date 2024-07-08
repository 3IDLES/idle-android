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
import com.idle.auth.center.CenterAuthEvent.NavigateTo
import com.idle.binding.DeepLinkDestination.CenterSignIn
import com.idle.binding.DeepLinkDestination.CenterSignUp
import com.idle.binding.deepLinkNavigateTo
import com.idle.binding.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterAuthFragment : Fragment() {

    private lateinit var composeView: ComposeView
    private val viewModel: CenterAuthViewModel by viewModels()

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
            CenterAuthScreen(
                navigateToCenterSignIn = { viewModel.event(NavigateTo(CenterSignIn)) },
                navigateToCenterSignUp = { viewModel.event(NavigateTo(CenterSignUp)) },
            )
        }
    }

    private fun handleEvent(event: CenterAuthEvent) = when (event) {
        is NavigateTo -> findNavController().deepLinkNavigateTo(requireContext(), event.destination)
    }
}


@Composable
internal fun CenterAuthScreen(
    navigateToCenterSignIn: () -> Unit,
    navigateToCenterSignUp: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
    ) {
        Text(text = "센터장님, 환영합니다!")

        Text(text = "기타 환영 멘트")

        Button(onClick = navigateToCenterSignIn) {
            Text(text = "로그인")
        }

        Button(onClick = navigateToCenterSignUp) {
            Text(text = "회원가입")
        }
    }
}