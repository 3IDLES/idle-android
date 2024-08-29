package com.idle.compose.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.binding.deepLinkNavigateTo
import com.idle.binding.repeatOnStarted
import com.idle.compose.base.navigation.JwtErrorNavigation
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseComposeFragment : Fragment() {

    protected abstract val fragmentViewModel: BaseViewModel
    protected var snackbarHostState = SnackbarHostState()
    private lateinit var composeView: ComposeView

    @Inject
    lateinit var jwtErrorNavigation: JwtErrorNavigation

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

    @OptIn(ExperimentalFoundationApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView.setContent {
            viewLifecycleOwner.repeatOnStarted {
                fragmentViewModel.baseEventFlow.collect {
                    handleEvent(it)
                }
            }

            CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                ComposeLayout()
            }
        }
    }

    private fun handleEvent(event: CareBaseEvent) {
        when (event) {
            is CareBaseEvent.NavigateTo -> findNavController()
                .deepLinkNavigateTo(
                    context = requireContext(),
                    deepLinkDestination = event.destination,
                    popUpTo = event.popUpTo,
                )

            is CareBaseEvent.ShowSnackBar -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(event.msg)
                }
            }

            is CareBaseEvent.JwtError -> jwtErrorNavigation.navigateToAuth()
        }
    }
}