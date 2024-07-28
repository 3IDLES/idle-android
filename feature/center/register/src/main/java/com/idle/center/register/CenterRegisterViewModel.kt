package com.idle.center.register

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.usecase.profile.RegisterCenterProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterVerificationViewModel @Inject constructor(
    private val registerCenterProfileUseCase: RegisterCenterProfileUseCase,
) : BaseViewModel() {

    private val _registerProcess = MutableStateFlow(RegisterProcess.INFO)
    val registerProcess = _registerProcess.asStateFlow()

    private val _centerName = MutableStateFlow("")
    val centerName = _centerName.asStateFlow()

    private val _centerNumber = MutableStateFlow("")
    val centerNumber = _centerNumber.asStateFlow()

    private val _centerIntroduce = MutableStateFlow("")
    val centerIntroduce = _centerIntroduce.asStateFlow()

    private val _centerAddress = MutableStateFlow("")
    val centerAddress = _centerAddress.asStateFlow()

    private val _centerDetailAddress = MutableStateFlow("")
    val centerDetailAddress = _centerDetailAddress.asStateFlow()

    private val _centerProfileImageUri = MutableStateFlow<Uri?>(null)
    val centerProfileImageUri = _centerProfileImageUri.asStateFlow()

    internal fun registerCenterProfile() = viewModelScope.launch {
        registerCenterProfileUseCase(
            centerName = _centerName.value,
            detailedAddress = _centerDetailAddress.value,
            introduce = _centerIntroduce.value,
            latitude = "",
            longitude = "",
            lotNumberAddress = "",
            officeNumber = _centerNumber.value,
            roadNameAddress = _centerAddress.value
        )
    }

    internal fun setRegisterProcess(process: RegisterProcess) {
        _registerProcess.value = process
    }

    internal fun setCenterName(name: String) {
        _centerName.value = name
    }

    internal fun setCenterNumber(phoneNumber: String) {
        _centerNumber.value = phoneNumber
    }

    internal fun setProfileImageUri(uri: Uri?) {
        _centerProfileImageUri.value = uri
    }

    internal fun setCenterIntroduce(introduce: String) {
        _centerIntroduce.value = introduce
    }

    internal fun setCenterAddress(address: String) {
        _centerAddress.value = address
    }

    internal fun setCenterDetailAddress(address: String) {
        _centerDetailAddress.value = address
    }
}

enum class RegisterProcess(val step: Int) {
    INFO(1), ADDRESS(2), INTRODUCE(3)
}