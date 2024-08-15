package com.idle.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.idle.presentation.R
import com.idle.setting.navigation.SettingNavigation
import javax.inject.Inject

class SettingNavigationImpl @Inject constructor(
    private val fragment: Fragment
) : SettingNavigation {
    override fun navigateToAuth() {
        fragment.findNavController().navigate(R.id.action_global_nav_auth)
    }
}