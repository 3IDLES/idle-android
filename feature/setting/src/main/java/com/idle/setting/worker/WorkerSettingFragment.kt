package com.idle.setting.worker

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.idle.binding.DeepLinkDestination.WorkerProfile
import com.idle.binding.base.BaseBindingFragment
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.binding.repeatOnStarted
import com.idle.setting.databinding.FragmentWorkerSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerSettingFragment :
    BaseBindingFragment<FragmentWorkerSettingBinding, WorkerSettingViewModel>(
        FragmentWorkerSettingBinding::inflate
    ) {
    override val fragmentViewModel: WorkerSettingViewModel by viewModels()

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

            }

            WorkerSettingEvent.Withdrawal -> {

            }

            WorkerSettingEvent.MyProfile -> fragmentViewModel.baseEvent(NavigateTo(WorkerProfile))
        }
    }
}