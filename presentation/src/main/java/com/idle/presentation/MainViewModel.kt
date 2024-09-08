package com.idle.presentation

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.config.ForceUpdate
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.usecase.config.GetForceUpdateInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getForceUpdateInfoUseCase: GetForceUpdateInfoUseCase,
) : BaseViewModel() {
    private val _navigationMenuType =
        MutableStateFlow<NavigationMenuType>(NavigationMenuType.Hide)
    val navigationMenuType = _navigationMenuType.asStateFlow()

    private val _forceUpdate = MutableStateFlow<ForceUpdate?>(null)
    val forceUpdate = _forceUpdate.asStateFlow()

    fun setNavigationMenuType(navigationMenuType: NavigationMenuType) {
        _navigationMenuType.value = navigationMenuType
    }

    fun getForceUpdateInfo() = viewModelScope.launch {
        getForceUpdateInfoUseCase().onSuccess {
            _forceUpdate.value = it
        }.onFailure {
            handleFailure(it as HttpResponseException)
        }
    }
}

sealed class NavigationMenuType {
    data object Center : NavigationMenuType()
    data object Worker : NavigationMenuType()
    data object Hide : NavigationMenuType()
}