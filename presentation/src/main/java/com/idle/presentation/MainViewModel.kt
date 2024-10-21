package com.idle.presentation

import androidx.lifecycle.viewModelScope
import com.idle.auth.R
import com.idle.binding.DeepLinkDestination.CenterHome
import com.idle.binding.DeepLinkDestination.CenterPending
import com.idle.binding.DeepLinkDestination.CenterRegister
import com.idle.binding.DeepLinkDestination.WorkerHome
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.EventHandler
import com.idle.binding.base.MainEvent
import com.idle.binding.base.MainEvent.NavigateTo
import com.idle.binding.base.MainEvent.ShowSnackBar
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.config.ForceUpdate
import com.idle.domain.model.error.ApiErrorCode
import com.idle.domain.model.error.ErrorHandler
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.profile.CenterManagerAccountStatus
import com.idle.domain.usecase.auth.GetAccessTokenUseCase
import com.idle.domain.usecase.auth.GetUserTypeUseCase
import com.idle.domain.usecase.config.GetForceUpdateInfoUseCase
import com.idle.domain.usecase.profile.GetCenterStatusUseCase
import com.idle.domain.usecase.profile.GetMyCenterProfileUseCase
import com.idle.domain.usecase.profile.GetMyWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getForceUpdateInfoUseCase: GetForceUpdateInfoUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getMyUserRoleUseCase: GetUserTypeUseCase,
    private val getMyCenterProfileUseCase: GetMyCenterProfileUseCase,
    private val getMyWorkerProfileUseCase: GetMyWorkerProfileUseCase,
    private val getCenterStatusUseCase: GetCenterStatusUseCase,
    private val errorHandler: ErrorHandler,
    private val eventHandler: EventHandler,
) : BaseViewModel() {
    private val _navigationMenuType = MutableStateFlow(NavigationMenuType.HIDE)
    val navigationMenuType = _navigationMenuType.asStateFlow()

    private val _forceUpdate = MutableStateFlow<ForceUpdate?>(null)
    val forceUpdate = _forceUpdate.asStateFlow()

    val eventFlow = eventHandler.eventFlow
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
        )

    init {
        handleError()
    }

    internal fun setNavigationMenuType(navigationMenuType: NavigationMenuType) {
        _navigationMenuType.value = navigationMenuType
    }

    internal fun getForceUpdateInfo() = viewModelScope.launch {
        getForceUpdateInfoUseCase().onSuccess {
            _forceUpdate.value = it
        }.onFailure { errorHandler.sendError(it) }
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
            UserType.WORKER.apiValue -> eventHandler.sendEvent(
                NavigateTo(
                    WorkerHome,
                    R.id.authFragment
                )
            )

            UserType.CENTER.apiValue -> getCenterStatus()
            else -> Unit
        }
    }

    private suspend fun getCenterStatus() =
        getCenterStatusUseCase().onSuccess { centerStatusResponse ->
            handleCenterStatus(centerStatusResponse.centerManagerAccountStatus)
        }

    private fun handleCenterStatus(status: CenterManagerAccountStatus) {
        when (status) {
            CenterManagerAccountStatus.APPROVED -> handleApprovedCenterStatus()
            else -> eventHandler.sendEvent(
                NavigateTo(CenterPending(status.name), R.id.authFragment)
            )
        }
    }

    private fun handleApprovedCenterStatus() = viewModelScope.launch {
        getMyCenterProfileUseCase().onSuccess {
            eventHandler.sendEvent(NavigateTo(CenterHome, R.id.authFragment))
        }.onFailure {
            val error = it as HttpResponseException
            if (error.apiErrorCode == ApiErrorCode.CenterNotFound) {
                eventHandler.sendEvent(NavigateTo(CenterRegister, R.id.authFragment))
            }
        }
    }

    private fun handleError() = viewModelScope.launch {
        errorHandler.errorEvent.collect { exception ->
            when (exception) {
                is HttpResponseException -> {
                    when (exception.apiErrorCode) {
                        ApiErrorCode.TokenDecodeException,
                        ApiErrorCode.TokenNotValid,
                        ApiErrorCode.TokenExpiredException,
                        ApiErrorCode.TokenNotFound,
                        ApiErrorCode.NotSupportUserTokenType ->
                            eventHandler.sendEvent(
                                MainEvent.NavigateToAuthWithClearBackStack(
                                    exception.print()
                                )
                            )

                        else -> eventHandler.sendEvent(ShowSnackBar(exception.print()))
                    }
                }

                is SocketTimeoutException -> eventHandler.sendEvent(ShowSnackBar("서버 응답 시간이 초과되었습니다. 잠시 후 다시 시도해 주세요."))
                is IOException -> eventHandler.sendEvent(ShowSnackBar("인터넷 연결이 불안정합니다. 네트워크 상태를 확인해 주세요."))
                else -> {}
            }
        }
    }
}

enum class NavigationMenuType {
    CENTER, WORKER, HIDE;
}
