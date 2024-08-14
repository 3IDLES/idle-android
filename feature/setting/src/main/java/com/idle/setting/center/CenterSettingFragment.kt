package com.idle.setting.center

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.idle.binding.DeepLinkDestination.CenterProfile
import com.idle.binding.base.BaseBindingFragment
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.binding.repeatOnStarted
import com.idle.setting.SettingEvent
import com.idle.setting.databinding.FragmentCenterSettingBinding
import com.idle.setting.dialog.LogoutDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterSettingFragment :
    BaseBindingFragment<FragmentCenterSettingBinding, CenterSettingViewModel>(
        FragmentCenterSettingBinding::inflate
    ) {
    override val fragmentViewModel: CenterSettingViewModel by viewModels()
    private val logoutDialog: LogoutDialogFragment? by lazy {
        LogoutDialogFragment().apply {
            onDismissCallback = {
                findNavController().currentBackStackEntry?.savedStateHandle?.let {
                    val result = it.get<Boolean>("logout")
                    if (result == true) fragmentViewModel.logout()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = fragmentViewModel
        fragmentViewModel.apply {
            viewLifecycleOwner.repeatOnStarted {
                centerSettingEvent.collect {
                    handleSettingEvent(it)
                }
            }
        }
    }

    private fun handleSettingEvent(event: SettingEvent) {
        when (event) {
            SettingEvent.Logout -> {
                if (!(logoutDialog?.isAdded == true || logoutDialog?.isVisible == true)) {
                    logoutDialog?.show(parentFragmentManager, "LogoutDialogFragment")
                }
            }

            SettingEvent.Withdrawal -> {

            }

            SettingEvent.Profile -> fragmentViewModel.baseEvent(NavigateTo(CenterProfile))

            SettingEvent.FAQ -> {}

            SettingEvent.PrivacyPolicy -> {}

            SettingEvent.Inquiry -> {}

            SettingEvent.TermsAndPolicies -> {}
        }
    }
}