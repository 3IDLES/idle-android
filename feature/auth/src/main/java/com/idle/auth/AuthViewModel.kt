package com.idle.auth

import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.domain.model.auth.UserType
import com.idle.domain.usecase.auth.GetAccessTokenUseCase
import com.idle.domain.usecase.auth.GetUserRoleUseCase
import com.idle.domain.usecase.profile.GetMyCenterProfileUseCase
import com.idle.domain.usecase.profile.GetMyWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getMyUserRoleUseCase: GetUserRoleUseCase,
    private val getMyCenterProfileUseCase: GetMyCenterProfileUseCase,
    private val getMyWorkerProfileUseCase: GetMyWorkerProfileUseCase,
) : BaseViewModel() {
    private val _userType = MutableStateFlow<UserType?>(null)
    val userRole = _userType.asStateFlow()

    init {
        viewModelScope.launch {
            val accessTokenDeferred = async { getAccessTokenUseCase() }
            val userRoleDeferred = async { getMyUserRoleUseCase() }

            val accessToken = accessTokenDeferred.await()
            val userRole = userRoleDeferred.await()

            if (accessToken.isBlank() || userRole.isBlank()) {
                return@launch
            }

            when (userRole) {
                UserType.CENTER.apiValue -> getMyCenterProfileUseCase().onFailure {
                    return@launch
                }

                UserType.WORKER.apiValue -> getMyWorkerProfileUseCase().onFailure {
                    return@launch
                }
            }

            navigateToDestination(userRole)
        }
    }

    private fun navigateToDestination(userRole: String) {
        val destination = when (userRole) {
            UserType.WORKER.apiValue -> DeepLinkDestination.WorkerHome
            UserType.CENTER.apiValue -> DeepLinkDestination.CenterHome
            else -> return
        }
        baseEvent(NavigateTo(destination, R.id.authFragment))
    }

    fun setUserRole(userType: UserType) {
        _userType.value = userType
    }
}