package com.idle.binding.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.idle.analytics.helper.AnalyticsHelper
import com.idle.binding.base.navigation.BaseNavigation
import com.idle.binding.deepLinkNavigateTo
import com.idle.binding.repeatOnStarted
import javax.inject.Inject

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseBindingFragment<T : ViewDataBinding, V : BaseViewModel>
    (private val inflate: Inflate<T>) : Fragment() {

    private var _binding: T? = null
    val binding get() = _binding!!

    protected abstract val fragmentViewModel: V

    @Inject
    lateinit var baseNavigation: BaseNavigation

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.repeatOnStarted {
            fragmentViewModel.baseEventFlow.collect {
                handleEvent(it)
            }
        }
    }

    private fun showSnackBar(message: String) {
        _binding?.root?.rootView?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
        }
    }

    private fun handleEvent(event: CareBaseEvent) = when (event) {
        is CareBaseEvent.NavigateTo -> findNavController()
            .deepLinkNavigateTo(
                context = requireContext(),
                deepLinkDestination = event.destination,
                popUpTo = event.popUpTo,
            )

        is CareBaseEvent.ShowSnackBar -> showSnackBar(event.msg)
        is CareBaseEvent.NavigateToAuthWithClearBackStack -> baseNavigation.navigateToAuth(event.snackBarMsg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}