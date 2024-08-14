package com.idle.setting.worker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.idle.binding.DeepLinkDestination
import com.idle.binding.DeepLinkDestination.Withdrawal
import com.idle.binding.DeepLinkDestination.WorkerProfile
import com.idle.binding.base.BaseBindingFragment
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.binding.repeatOnStarted
import com.idle.setting.SettingEvent
import com.idle.setting.databinding.FragmentWorkerSettingBinding
import com.idle.setting.dialog.LogoutDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerSettingFragment :
    BaseBindingFragment<FragmentWorkerSettingBinding, WorkerSettingViewModel>(
        FragmentWorkerSettingBinding::inflate
    ) {
    override val fragmentViewModel: WorkerSettingViewModel by viewModels()
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
                workerSettingEvent.collect {
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
            SettingEvent.Withdrawal -> fragmentViewModel.baseEvent(NavigateTo(Withdrawal))
            SettingEvent.Profile -> fragmentViewModel.baseEvent(NavigateTo(WorkerProfile))
            SettingEvent.FAQ -> navigateToUri("https://grove-maraca-55d.notion.site/8579186ee8ca4dbb8dc55e3b8b744d11?pvs=4")
            SettingEvent.PrivacyPolicy -> navigateToUri("https://grove-maraca-55d.notion.site/ad4f62dff5304d63a162f1269639afca?pvs=4")
            SettingEvent.TermsAndPolicies -> navigateToUri("https://grove-maraca-55d.notion.site/2e4d597aff1f406e9164cdb6f9195de0?pvs=4")
            SettingEvent.Inquiry -> {}
        }
    }

    private fun navigateToUri(url: String) =
        startActivity(Intent(Intent.ACTION_VIEW, url.toUri()), null)
}