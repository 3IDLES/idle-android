package com.idle.setting.center

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.idle.binding.DeepLinkDestination.CenterProfile
import com.idle.binding.base.BaseBindingFragment
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.binding.repeatOnStarted
import com.idle.setting.databinding.FragmentCenterSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterSettingFragment :
    BaseBindingFragment<FragmentCenterSettingBinding, CenterSettingViewModel>(
        FragmentCenterSettingBinding::inflate
    ) {
    override val fragmentViewModel: CenterSettingViewModel by viewModels()

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

    private fun handleSettingEvent(event: CenterSettingEvent) {
        when (event) {
            CenterSettingEvent.Logout -> {

            }

            CenterSettingEvent.Withdrawal -> {

            }

            CenterSettingEvent.MyCenter -> fragmentViewModel.baseEvent(NavigateTo(CenterProfile))
        }
    }
}