package com.idle.center.register.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.binding.EventHelper
import com.idle.binding.MainEvent
import com.idle.common.suspendRunCatching
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.repositorry.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterCenterInfoCompleteViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val eventHelper: EventHelper,
    val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
    private val _centerProfile = MutableStateFlow<CenterProfile?>(null)
    val centerProfile = _centerProfile.asStateFlow()

    init {
        viewModelScope.launch {
            suspendRunCatching {
                profileRepository.getMyCenterProfile()
            }.onSuccess {
                _centerProfile.value = it
            }.onFailure {
                eventHelper.sendEvent(MainEvent.ShowToast(it.toString()))
            }
        }
    }
}
