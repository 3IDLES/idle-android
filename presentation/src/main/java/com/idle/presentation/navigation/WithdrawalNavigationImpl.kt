package com.idle.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.idle.presentation.R
import com.idle.compose.base.navigation.WithdrawalNavigation
import javax.inject.Inject

class WithdrawalNavigationImpl @Inject constructor(
    private val fragment: Fragment
) : WithdrawalNavigation {
    override fun navigateToAuth() {
        fragment.findNavController().navigate(R.id.action_global_nav_auth)
    }
}