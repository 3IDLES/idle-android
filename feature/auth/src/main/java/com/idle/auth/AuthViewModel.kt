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
class AuthViewModel @Inject constructor() : BaseViewModel() {
    private val _userType = MutableStateFlow<UserType?>(null)
    val userRole = _userType.asStateFlow()

    internal fun setUserRole(userType: UserType) {
        _userType.value = userType
    }
}