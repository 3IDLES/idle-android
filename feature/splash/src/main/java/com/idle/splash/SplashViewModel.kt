package com.idle.splash

import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel() {
    init {
        viewModelScope.launch {
            delay(2000L)
            baseEvent(CareBaseEvent.NavigateTo(DeepLinkDestination.Auth, popUpTo = R.id.nav_splash))
        }
    }
}