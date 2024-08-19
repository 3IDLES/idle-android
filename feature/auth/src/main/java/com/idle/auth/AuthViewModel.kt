package com.idle.auth

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.domain.model.auth.UserRole
import com.idle.domain.usecase.auth.GetAccessTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getUserRoleUseCase : GetUserRoleUseCase,
) : BaseViewModel() {
    private val _userRole = MutableStateFlow<UserRole?>(null)
    val userRole = _userRole.asStateFlow()

    fun setUserRole(userRole: UserRole){
        _userRole.value = userRole
    }

    fun handleTokenNavigation(
        defaultDestination: NavigateTo,
        authenticatedDestination: NavigateTo
    ) = viewModelScope.launch(Dispatchers.IO) {
        val accessToken = getAccessTokenUseCase()

        if (accessToken.isNotBlank()) {
            baseEvent(authenticatedDestination)
            return@launch
        }

        baseEvent(defaultDestination)
    }
}