package com.idle.auth

import com.idle.binding.EventHandler
import com.idle.binding.NavigationRouter
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.auth.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val eventHandler: EventHandler,
    val navigationRouter: NavigationRouter,
) : BaseViewModel() {
    private val _userType = MutableStateFlow<UserType?>(null)
    val userRole = _userType.asStateFlow()

    internal fun setUserRole(userType: UserType) {
        _userType.value = userType
    }
}