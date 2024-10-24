package com.idle.center.register.complete

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.EventHandlerHelper
import com.idle.binding.MainEvent
import com.idle.binding.NavigationHelper
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.usecase.profile.GetLocalMyCenterProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterCenterInfoCompleteViewModel @Inject constructor(
    private val getLocalMyCenterProfileUseCase: GetLocalMyCenterProfileUseCase,
    private val eventHandlerHelper: EventHandlerHelper,
    val navigationHelper: NavigationHelper,
) : BaseViewModel() {
    private val _centerProfile = MutableStateFlow<CenterProfile?>(null)
    val centerProfile = _centerProfile.asStateFlow()

    init {
        viewModelScope.launch {
            getLocalMyCenterProfileUseCase()
                .onSuccess { _centerProfile.value = it }
                .onFailure {
                    eventHandlerHelper.sendEvent(MainEvent.ShowToast(it.toString()))
                }
        }
    }
}