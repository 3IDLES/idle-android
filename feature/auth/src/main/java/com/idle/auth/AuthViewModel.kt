package com.idle.auth

import com.idle.binding.EventHandlerHelper
import com.idle.binding.NavigationHelper
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.auth.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val eventHandlerHelper: EventHandlerHelper,
    val navigationHelper: NavigationHelper,
) : BaseViewModel() {
    private val _userType = MutableStateFlow<UserType?>(null)
    val userRole = _userType.asStateFlow()

    internal fun setUserRole(userType: UserType) {
        _userType.value = userType
    }
}