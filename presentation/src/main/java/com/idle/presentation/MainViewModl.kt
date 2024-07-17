package com.idle.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _navigationMenuType = MutableStateFlow(NavigationMenuType.NONE)
    val navigationMenuType = _navigationMenuType.asStateFlow()

    fun setNavigationMenuType(navigationMenuType: NavigationMenuType) {
        _navigationMenuType.value = navigationMenuType    }
}

enum class NavigationMenuType {
    CENTER, WORKER, NONE
}