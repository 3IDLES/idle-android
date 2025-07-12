package com.idle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.auth.R
import com.idle.binding.EventHelper
import com.idle.binding.MainEvent.ShowToast
import com.idle.common.suspendRunCatching
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.config.ForceUpdate
import com.idle.domain.model.error.ApiErrorCode
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.jobposting.SharedJobPostingInfo
import com.idle.domain.model.profile.CenterManagerAccountStatus
import com.idle.domain.repositorry.ConfigRepository
import com.idle.domain.repositorry.JobPostingRepository
import com.idle.domain.repositorry.NotificationRepository
import com.idle.domain.repositorry.ProfileRepository
import com.idle.domain.repositorry.TokenRepository
import com.idle.navigation.DeepLinkDestination.CenterHome
import com.idle.navigation.DeepLinkDestination.CenterPending
import com.idle.navigation.DeepLinkDestination.CenterRegister
import com.idle.navigation.DeepLinkDestination.WorkerHome
import com.idle.navigation.NavigationEvent
import com.idle.navigation.NavigationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val tokenRepository: TokenRepository,
    private val profileRepository: ProfileRepository,
    private val notificationRepository: NotificationRepository,
    private val jobPostingRepository: JobPostingRepository,
    private val errorHelper: ErrorHelper,
    internal val eventHelper: EventHelper,
    internal val navigationHelper: NavigationHelper,
) : ViewModel() {
    private val _navigationMenuType = MutableStateFlow(NavigationMenuType.HIDE)
    val navigationMenuType = _navigationMenuType.asStateFlow()

    private val _forceUpdate = MutableStateFlow<ForceUpdate?>(null)
    val forceUpdate = _forceUpdate.asStateFlow()

    init {
        handleError()
    }

    internal fun setNavigationMenuType(navigationMenuType: NavigationMenuType) {
        _navigationMenuType.value = navigationMenuType
    }

    internal fun getForceUpdateInfo() = viewModelScope.launch {
        suspendRunCatching {
            configRepository.getForceUpdate()
        }.onSuccess {
            _forceUpdate.value = it
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun initializeUserSession() = viewModelScope.launch {
        val (accessToken, userRole) = getAccessTokenAndUserRole()

        if (accessToken.isBlank() || userRole.isBlank()) {
            return@launch
        }

        if (userRole == UserType.WORKER.apiValue) {
            suspendRunCatching {
                profileRepository.getMyWorkerProfile()
            }.onFailure {
                return@launch
            }
        }

        navigateToDestination(userRole)
    }

    internal fun setSharedJobPostingInfo(sharedJobPostingInfo: SharedJobPostingInfo) {
        jobPostingRepository.sharedJobPostingInfo = sharedJobPostingInfo
    }

    internal fun readNotification(notificationId: String) = viewModelScope.launch {
        suspendRunCatching {
            notificationRepository.readNotification(notificationId)
        }.onFailure { errorHelper.sendError(it) }
    }

    private suspend fun getAccessTokenAndUserRole(): Pair<String, String> = coroutineScope {
        val accessTokenDeferred = async { tokenRepository.getAccessToken() }
        val userRoleDeferred = async { profileRepository.getMyUserType() }
        accessTokenDeferred.await() to userRoleDeferred.await()
    }

    private suspend fun navigateToDestination(userRole: String) {
        when (userRole) {
            UserType.WORKER.apiValue -> navigationHelper.navigateTo(
                NavigationEvent.To(
                    WorkerHome,
                    R.id.authFragment
                )
            )

            UserType.CENTER.apiValue -> getCenterStatus()
            else -> Unit
        }
    }

    private suspend fun getCenterStatus() = suspendRunCatching {
        profileRepository.getCenterStatus()
    }.onSuccess { centerStatusResponse ->
        handleCenterStatus(centerStatusResponse.centerManagerAccountStatus)
    }

    private fun handleCenterStatus(status: CenterManagerAccountStatus) {
        when (status) {
            CenterManagerAccountStatus.APPROVED -> handleApprovedCenterStatus()
            else -> navigationHelper.navigateTo(
                NavigationEvent.To(
                    CenterPending(status.name),
                    R.id.authFragment
                )
            )
        }
    }

    private fun handleApprovedCenterStatus() = viewModelScope.launch {
        suspendRunCatching {
            profileRepository.getMyCenterProfile()
        }.onSuccess {
            navigationHelper.navigateTo(NavigationEvent.To(CenterHome, R.id.authFragment))
        }.onFailure {
            val error = it as HttpResponseException
            if (error.apiErrorCode == ApiErrorCode.CenterNotFound) {
                navigationHelper.navigateTo(
                    NavigationEvent.To(
                        CenterRegister,
                        R.id.authFragment
                    )
                )
            }
        }
    }

    private fun handleError() = viewModelScope.launch {
        errorHelper.errorEvent.collect { exception ->
            when (exception) {
                is HttpResponseException -> {
                    when (exception.apiErrorCode) {
                        ApiErrorCode.TokenDecodeException,
                        ApiErrorCode.TokenNotValid,
                        ApiErrorCode.TokenExpiredException,
                        ApiErrorCode.TokenNotFound,
                        ApiErrorCode.NotSupportUserTokenType -> {
                            navigationHelper.navigateTo(
                                NavigationEvent.ToAuthWithClearBackStack(
                                    exception.print()
                                )
                            )
                        }

                        else -> eventHelper.sendEvent(ShowToast(exception.print()))
                    }
                    return@collect
                }

                is SocketTimeoutException -> eventHelper.sendEvent(ShowToast("서버 응답 시간이 초과되었습니다. 잠시 후 다시 시도해 주세요."))
                is IOException -> eventHelper.sendEvent(ShowToast("인터넷 연결이 불안정합니다. 네트워크 상태를 확인해 주세요."))
                else -> {}
            }
        }
    }
}

enum class NavigationMenuType {
    CENTER, WORKER, HIDE;
}
