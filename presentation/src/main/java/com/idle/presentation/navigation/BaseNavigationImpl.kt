package com.idle.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.idle.auth.AuthFragmentDirections
import com.idle.binding.base.navigation.BaseNavigation
import javax.inject.Inject

class BaseNavigationImpl @Inject constructor(
    private val fragment: Fragment
) : BaseNavigation {
    override fun navigateToAuth(snackBarMsg: String) {
        fragment.findNavController()
            .navigate(AuthFragmentDirections.actionGlobalNavAuth(snackBarMsg))
    }
}