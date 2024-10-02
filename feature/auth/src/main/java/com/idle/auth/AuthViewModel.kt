package com.idle.auth

import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination.CenterHome
import com.idle.binding.DeepLinkDestination.CenterPending
import com.idle.binding.DeepLinkDestination.CenterRegister
import com.idle.binding.DeepLinkDestination.WorkerHome
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.error.ApiErrorCode
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.profile.CenterManagerAccountStatus
import com.idle.domain.usecase.auth.GetAccessTokenUseCase
import com.idle.domain.usecase.auth.GetUserTypeUseCase
import com.idle.domain.usecase.profile.GetCenterStatusUseCase
import com.idle.domain.usecase.profile.GetMyCenterProfileUseCase
import com.idle.domain.usecase.profile.GetMyWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getMyUserRoleUseCase: GetUserTypeUseCase,
    private val getMyCenterProfileUseCase: GetMyCenterProfileUseCase,
    private val getMyWorkerProfileUseCase: GetMyWorkerProfileUseCase,
    private val getCenterStatusUseCase: GetCenterStatusUseCase,
) : BaseViewModel() {
    private val _userType = MutableStateFlow<UserType?>(null)
    val userRole = _userType.asStateFlow()

    init {
        initializeUserSession()
    }

    private fun initializeUserSession() = viewModelScope.launch {
        val (accessToken, userRole) = getAccessTokenAndUserRole()

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

    private suspend fun getAccessTokenAndUserRole(): Pair<String, String> = coroutineScope {
        val accessTokenDeferred = async { getAccessTokenUseCase() }
        val userRoleDeferred = async { getMyUserRoleUseCase() }
        accessTokenDeferred.await() to userRoleDeferred.await()
    }

    private fun navigateToDestination(userRole: String) {
        when (userRole) {
            UserType.WORKER.apiValue -> baseEvent(NavigateTo(WorkerHome, R.id.authFragment))
            UserType.CENTER.apiValue -> getCenterStatus()
            else -> Unit
        }
    }

    private fun getCenterStatus() = viewModelScope.launch {
        getCenterStatusUseCase().onSuccess { centerStatusResponse ->
            handleCenterStatus(centerStatusResponse.centerManagerAccountStatus)
        }
    }

    private fun handleCenterStatus(status: CenterManagerAccountStatus) = when (status) {
        CenterManagerAccountStatus.APPROVED -> handleApprovedCenterStatus()
        else -> baseEvent(NavigateTo(CenterPending(status.name), R.id.authFragment))
    }

    private fun handleApprovedCenterStatus() = viewModelScope.launch {
        getMyCenterProfileUseCase().onSuccess {
            baseEvent(NavigateTo(CenterHome, R.id.authFragment))
        }.onFailure {
            val error = it as HttpResponseException
            if (error.apiErrorCode == ApiErrorCode.CenterNotFound) {
                baseEvent(NavigateTo(CenterRegister, R.id.authFragment))
            }
        }
    }

    internal fun setUserRole(userType: UserType) {
        _userType.value = userType
    }
}