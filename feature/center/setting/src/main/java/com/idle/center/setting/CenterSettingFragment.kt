package com.idle.center.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.idle.binding.base.BaseBindingFragment
import com.idle.center.setting.databinding.FragmentCenterSettingBinding
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
    }
}