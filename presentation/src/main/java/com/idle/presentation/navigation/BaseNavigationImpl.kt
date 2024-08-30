package com.idle.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.idle.binding.base.navigation.BaseNavigation
import com.idle.presentation.R
import javax.inject.Inject

class BaseNavigationImpl @Inject constructor(
    private val fragment: Fragment
) : BaseNavigation {
    override fun navigateToAuth() {
        fragment.findNavController().navigate(R.id.action_global_nav_auth)
    }
}