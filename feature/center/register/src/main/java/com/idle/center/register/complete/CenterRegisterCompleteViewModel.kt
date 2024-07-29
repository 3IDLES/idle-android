package com.idle.center.register.complete

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.usecase.profile.GetMyCenterProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterRegisterCompleteViewModel @Inject constructor(
    private val getMyCenterProfileUseCase: GetMyCenterProfileUseCase,
) : BaseViewModel() {
    private val _centerProfile = MutableStateFlow(CenterProfile())
    val centerProfile = _centerProfile.asStateFlow()

    init {
        viewModelScope.launch {
            getMyCenterProfileUseCase()
                .onSuccess { _centerProfile.value = it }
                .onFailure { Log.d("test", "센터 정보 조회 실패! $it") }
        }
    }
}