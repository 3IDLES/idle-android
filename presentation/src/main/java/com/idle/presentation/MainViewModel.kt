package com.idle.presentation

import androidx.lifecycle.viewModelScope
import com.idle.auth.R
import com.idle.binding.DeepLinkDestination
import com.idle.binding.DeepLinkDestination.CenterHome
import com.idle.binding.DeepLinkDestination.CenterPending
import com.idle.binding.DeepLinkDestination.CenterRegister
import com.idle.binding.DeepLinkDestination.WorkerHome
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.config.ForceUpdate
import com.idle.domain.model.error.ApiErrorCode
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.profile.CenterManagerAccountStatus
import com.idle.domain.usecase.auth.GetAccessTokenUseCase
import com.idle.domain.usecase.auth.GetUserTypeUseCase
import com.idle.domain.usecase.config.GetForceUpdateInfoUseCase
import com.idle.domain.usecase.profile.GetCenterStatusUseCase
import com.idle.domain.usecase.profile.GetMyCenterProfileUseCase
import com.idle.domain.usecase.profile.GetMyWorkerProfileUseCase
import com.idle.presentation.MainEvent.NavigateTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getForceUpdateInfoUseCase: GetForceUpdateInfoUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getMyUserRoleUseCase: GetUserTypeUseCase,
    private val getMyCenterProfileUseCase: GetMyCenterProfileUseCase,
    private val getMyWorkerProfileUseCase: GetMyWorkerProfileUseCase,
    private val getCenterStatusUseCase: GetCenterStatusUseCase,
) : BaseViewModel() {
    private val _navigationMenuType = MutableStateFlow(NavigationMenuType.HIDE)
    val navigationMenuType = _navigationMenuType.asStateFlow()

    private val _forceUpdate = MutableStateFlow<ForceUpdate?>(null)
    val forceUpdate = _forceUpdate.asStateFlow()

    private val _eventFlow = MutableSharedFlow<MainEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private fun event(event: MainEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }

    internal fun setNavigationMenuType(navigationMenuType: NavigationMenuType) {
        _navigationMenuType.value = navigationMenuType
    }

    internal fun getForceUpdateInfo() = viewModelScope.launch {
        getForceUpdateInfoUseCase().onSuccess {
            _forceUpdate.value = it
        }.onFailure {
            handleFailure(it as HttpResponseException)
        }
    }

    internal fun initializeUserSession() = viewModelScope.launch {
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

    private suspend fun navigateToDestination(userRole: String) {
        when (userRole) {
            UserType.WORKER.apiValue -> event(NavigateTo(WorkerHome, R.id.authFragment))
            UserType.CENTER.apiValue -> getCenterStatus()
            else -> Unit
        }
    }

    private suspend fun getCenterStatus() =
        getCenterStatusUseCase().onSuccess { centerStatusResponse ->
            handleCenterStatus(centerStatusResponse.centerManagerAccountStatus)
        }

    private fun handleCenterStatus(status: CenterManagerAccountStatus) = when (status) {
        CenterManagerAccountStatus.APPROVED -> handleApprovedCenterStatus()
        else -> event(NavigateTo(CenterPending(status.name), R.id.authFragment))
    }

    private fun handleApprovedCenterStatus() = viewModelScope.launch {
        getMyCenterProfileUseCase().onSuccess {
            event(NavigateTo(CenterHome, R.id.authFragment))
        }.onFailure {
            val error = it as HttpResponseException
            if (error.apiErrorCode == ApiErrorCode.CenterNotFound) {
                event(NavigateTo(CenterRegister, R.id.authFragment))
            }
        }
    }
}

enum class NavigationMenuType {
    CENTER, WORKER, HIDE;
}

sealed class MainEvent {
    data class NavigateTo(val destination: DeepLinkDestination, val popUpTo: Int? = null) :
        MainEvent()
}