package com.idle.auth

import androidx.lifecycle.ViewModel
import com.idle.binding.EventHelper
import com.idle.domain.model.auth.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val eventHelper: EventHelper,
    val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
    private val _userType = MutableStateFlow<UserType?>(null)
    val userRole = _userType.asStateFlow()

    internal fun setUserRole(userType: UserType) {
        _userType.value = userType
    }
}
