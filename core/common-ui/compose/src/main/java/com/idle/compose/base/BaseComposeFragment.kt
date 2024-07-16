package com.idle.compose.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.binding.deepLinkNavigateTo
import com.idle.binding.repeatOnStarted

abstract class BaseComposeFragment : Fragment() {

    abstract val viewModel: BaseViewModel
    private lateinit var composeView: ComposeView

    @Composable
    abstract fun ComposeLayout()

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

        viewLifecycleOwner.repeatOnStarted { viewModel.baseEventFlow.collect { handleEvent(it) } }

        composeView.setContent {
            ComposeLayout()
        }
    }

    private fun handleEvent(event: CareBaseEvent) = when (event) {
        is CareBaseEvent.NavigateTo -> findNavController()
            .deepLinkNavigateTo(requireContext(), event.destination)
    }
}