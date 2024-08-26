package com.idle.center.profile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.usecase.profile.GetLocalMyCenterProfileUseCase
import com.idle.domain.usecase.profile.UpdateCenterProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterProfileViewModel @Inject constructor(
    private val getLocalMyCenterProfileUseCase: GetLocalMyCenterProfileUseCase,
    private val updateCenterProfileUseCase: UpdateCenterProfileUseCase,
) : BaseViewModel() {

    private val _centerProfile = MutableStateFlow<CenterProfile?>(null)
    val centerProfile = _centerProfile.asStateFlow()

    private val _centerOfficeNumber = MutableStateFlow("")
    val centerOfficeNumber = _centerOfficeNumber.asStateFlow()

    private val _centerIntroduce = MutableStateFlow("")
    val centerIntroduce = _centerIntroduce.asStateFlow()

    private val _isEditState = MutableStateFlow(false)
    val isEditState = _isEditState.asStateFlow()

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri = _profileImageUri.asStateFlow()

    init {
        getMyCenterProfile()
    }

    private fun getMyCenterProfile() = viewModelScope.launch {
        getLocalMyCenterProfileUseCase().onSuccess {
            _centerProfile.value = it
            _centerIntroduce.value = it.introduce ?: ""
            _centerOfficeNumber.value = it.officeNumber
        }.onFailure { baseEvent(CareBaseEvent.Error(it.message.toString())) }
    }

    fun updateCenterProfile() = viewModelScope.launch {
        if (_centerOfficeNumber.value.isBlank()) {
            return@launch
        }

        if (isCenterProfileUnchanged()) {
            setEditState(false)
            return@launch
        }

        updateCenterProfileUseCase(
            officeNumber = _centerOfficeNumber.value,
            introduce = _centerIntroduce.value.ifBlank { null },
            imageFileUri = _profileImageUri.value?.toString(),
        ).onSuccess {
            setEditState(false)
        }.onFailure { baseEvent(CareBaseEvent.Error(it.message.toString())) }
    }

    private fun isCenterProfileUnchanged(): Boolean {
        return _centerOfficeNumber.value == _centerProfile.value?.officeNumber &&
                _centerIntroduce.value == _centerProfile.value?.introduce &&
                profileImageUri.value == null
    }

    fun setCenterOfficeNumber(number: String) {
        _centerOfficeNumber.value = number
    }

    fun setCenterIntroduce(introduce: String) {
        _centerIntroduce.value = introduce
    }

    fun setEditState(state: Boolean) {
        _isEditState.value = state
    }

    fun setProfileImageUrl(uri: Uri?) {
        _profileImageUri.value = uri
    }
}