package com.idle.center.profile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.idle.binding.EventHandlerHelper
import com.idle.binding.MainEvent
import com.idle.binding.ToastType.SUCCESS
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.error.ErrorHandlerHelper
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.usecase.profile.GetCenterProfileUseCase
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
    private val getCenterProfileUseCase: GetCenterProfileUseCase,
    private val updateCenterProfileUseCase: UpdateCenterProfileUseCase,
    private val errorHandlerHelper: ErrorHandlerHelper,
    private val eventHandlerHelper: EventHandlerHelper,
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

    private val _isUpdateLoading = MutableStateFlow(false)
    val isUpdateLoading = _isUpdateLoading.asStateFlow()

    internal fun setCenterOfficeNumber(number: String) {
        _centerOfficeNumber.value = number
    }

    internal fun setCenterIntroduce(introduce: String) {
        _centerIntroduce.value = introduce
    }

    internal fun setEditState(state: Boolean) {
        _isEditState.value = state
        if (!state) {
            getMyCenterProfile()
        }
    }

    internal fun setProfileImageUrl(uri: Uri?) {
        _profileImageUri.value = uri
    }

    internal fun getMyCenterProfile() = viewModelScope.launch {
        getLocalMyCenterProfileUseCase().onSuccess {
            _centerProfile.value = it
            _centerIntroduce.value = it.introduce ?: ""
            _centerOfficeNumber.value = it.officeNumber
        }.onFailure { errorHandlerHelper.sendError(it) }
    }

    internal fun getCenterProfile(centerId: String) = viewModelScope.launch {
        getCenterProfileUseCase(centerId).onSuccess {
            _centerProfile.value = it
            _centerIntroduce.value = it.introduce ?: ""
            _centerOfficeNumber.value = it.officeNumber
        }.onFailure { errorHandlerHelper.sendError(it) }
    }

    internal fun updateCenterProfile() = viewModelScope.launch {
        if (_centerOfficeNumber.value.isBlank()) {
            return@launch
        }

        if (isCenterProfileUnchanged()) {
            setEditState(false)
            return@launch
        }

        _isUpdateLoading.value = true

        updateCenterProfileUseCase(
            officeNumber = _centerOfficeNumber.value,
            introduce = _centerIntroduce.value.ifBlank { null },
            imageFileUri = _profileImageUri.value?.toString(),
        ).onSuccess {
            eventHandlerHelper.sendEvent(MainEvent.ShowToast("정보 수정이 완료되었어요.", SUCCESS))
            setEditState(false)
        }.onFailure {
            errorHandlerHelper.sendError(it)
        }.also { _isUpdateLoading.value = false }
    }

    private fun isCenterProfileUnchanged(): Boolean {
        return _centerOfficeNumber.value == _centerProfile.value?.officeNumber &&
                _centerIntroduce.value == _centerProfile.value?.introduce &&
                profileImageUri.value == null
    }
}