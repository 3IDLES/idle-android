package com.idle.setting.worker

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.idle.binding.DeepLinkDestination.WorkerProfile
import com.idle.binding.base.BaseBindingFragment
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.binding.repeatOnStarted
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

    private fun handleSettingEvent(event: WorkerSettingEvent) {
        when (event) {
            WorkerSettingEvent.Logout -> {
                if (!(logoutDialog?.isAdded == true || logoutDialog?.isVisible == true)) {
                    logoutDialog?.show(parentFragmentManager, "LogoutDialogFragment")
                }
            }

            WorkerSettingEvent.Withdrawal -> {

            }

            WorkerSettingEvent.MyProfile -> fragmentViewModel.baseEvent(NavigateTo(WorkerProfile))
        }
    }
}