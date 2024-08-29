package com.idle.auth

import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.domain.model.auth.UserType
import com.idle.domain.usecase.auth.GetAccessTokenUseCase
import com.idle.domain.usecase.auth.GetUserRoleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getMyUserRoleUseCase: GetUserRoleUseCase,
) : BaseViewModel() {
    private val _userType = MutableStateFlow<UserType?>(null)
    val userRole = _userType.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        baseEvent(CareBaseEvent.ShowSnackBar(throwable.message.toString()))
    }

    init {
        viewModelScope.launch(exceptionHandler) {
            val accessTokenDeferred = async { getAccessTokenUseCase() }
            val userRoleDeferred = async { getMyUserRoleUseCase() }

            val accessToken = accessTokenDeferred.await()
            val userRole = userRoleDeferred.await()

            if (accessToken.isBlank() || userRole.isBlank()) {
                return@launch
            }

            // Todo:토큰이 실제 서버에서 유효한지 확인하는 추가 로직 필요

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