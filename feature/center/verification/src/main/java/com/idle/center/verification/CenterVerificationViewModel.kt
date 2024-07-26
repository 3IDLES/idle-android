package com.idle.center.verification

import android.net.Uri
import com.idle.binding.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CenterVerificationViewModel @Inject constructor() : BaseViewModel() {

    private val _verificationProcess = MutableStateFlow(VerificationProcess.INFO)
    val verificationProcess = _verificationProcess.asStateFlow()

    private val _centerName = MutableStateFlow("")
    val centerName = _centerName.asStateFlow()

    private val _centerNumber = MutableStateFlow("")
    val centerNumber = _centerNumber.asStateFlow()

    private val _centerIntroduce = MutableStateFlow("")
    val centerIntroduce = _centerIntroduce.asStateFlow()

    private val _centerProfileImageUri = MutableStateFlow<Uri?>(null)
    val centerProfileImageUri = _centerProfileImageUri.asStateFlow()

    internal fun setVerificationProcess(process: VerificationProcess) {
        _verificationProcess.value = process
    }

    internal fun setCenterName(name: String) {
        _centerName.value = name
    }

    internal fun setCenterNumber(phoneNumber: String) {
        _centerNumber.value = phoneNumber
    }

    internal fun setCenterProfileImageUri(uri: Uri) {
        _centerProfileImageUri.value = uri
    }

    internal fun setCenterIntroduce(introduce: String) {
        _centerIntroduce.value = introduce
    }
}

enum class VerificationProcess(val step: Int) {
    INFO(1), ADDRESS(2), INTRODUCE(3)
}