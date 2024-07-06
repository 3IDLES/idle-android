package com.idle.signin.center

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterSignInFragment : Fragment() {

    private lateinit var composeView: ComposeView
    private val viewModel: CenterSignInViewModel by viewModels()

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
                val centerId by centerId.collectAsStateWithLifecycle()
                val centerPassword by centerPassword.collectAsStateWithLifecycle()

                CenterSignInScreen(
                    centerId = centerId,
                    centerPassword = centerPassword,
                    onCenterIdChanged = ::setCenterId,
                    onCenterPasswordChanged = ::setCenterPassword,
                )
            }
        }
    }

    private fun handleEvent(event: CenterSignInEvent) = when (event) {
        is CenterSignInEvent.NavigateTo -> findNavController()
            .deepLinkNavigateTo(requireContext(), event.destination)
    }
}


@Composable
internal fun CenterSignInScreen(
    centerId: String,
    centerPassword: String,
    onCenterIdChanged: (String) -> Unit,
    onCenterPasswordChanged: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(text = "아이디 설정 (영문+숫자 조합 10자리 이상 등 조건)")

            TextField(
                value = centerId,
                onValueChange = onCenterIdChanged,
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(text = "비밀번호 설정 (영문+숫자 조합 10자리 이상 등 조건)")

            TextField(
                value = centerPassword,
                onValueChange = onCenterPasswordChanged
            )
        }

        Text(text = "비밀번호가 기억나지 않나요?",
            modifier = Modifier.clickable {})

        Button(onClick = {}) {
            Text(text = "로그인")
        }
    }
}