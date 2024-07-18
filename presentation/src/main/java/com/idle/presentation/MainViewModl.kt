package com.idle.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _navigationMenuType =
        MutableStateFlow<NavigationMenuType>(NavigationMenuType.Hide)
    val navigationMenuType = _navigationMenuType.asStateFlow()

    fun setNavigationMenuType(navigationMenuType: NavigationMenuType) {
        _navigationMenuType.value = navigationMenuType
    }
}

sealed class NavigationMenuType {
    data class Center(val destinationId: Int) : NavigationMenuType()
    data class Worker(val destinationId: Int) : NavigationMenuType()
    data object Hide : NavigationMenuType()
}