package com.idle.center.register

import android.net.Uri
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination.CenterRegisterComplete
import com.idle.binding.NavigationEvent
import com.idle.binding.NavigationHelper
import com.idle.binding.base.BaseViewModel
import com.idle.center.register.info.R
import com.idle.domain.model.error.ErrorHandlerHelper
import com.idle.domain.usecase.profile.RegisterCenterProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterCenterInfoViewModel @Inject constructor(
    private val registerCenterProfileUseCase: RegisterCenterProfileUseCase,
    private val errorHandlerHelper: ErrorHandlerHelper,
    val navigationHelper: NavigationHelper,
) : BaseViewModel() {

    private val _registrationStep = MutableStateFlow(RegistrationStep.INFO)
    val registrationStep = _registrationStep.asStateFlow()

    private val _centerName = MutableStateFlow("")
    val centerName = _centerName.asStateFlow()

    private val _centerNumber = MutableStateFlow("")
    val centerNumber = _centerNumber.asStateFlow()

    private val _centerIntroduce = MutableStateFlow("")
    val centerIntroduce = _centerIntroduce.asStateFlow()

    private val _roadNameAddress = MutableStateFlow("")
    val roadNameAddress = _roadNameAddress.asStateFlow()

    private val _lotNumberAddress = MutableStateFlow("")

    private val _centerDetailAddress = MutableStateFlow("")
    val centerDetailAddress = _centerDetailAddress.asStateFlow()

    private val _centerProfileImageUri = MutableStateFlow<Uri?>(null)
    val centerProfileImageUri = _centerProfileImageUri.asStateFlow()

    internal fun registerCenterProfile() = viewModelScope.launch {
        registerCenterProfileUseCase(
            centerName = _centerName.value,
            detailedAddress = _centerDetailAddress.value,
            introduce = _centerIntroduce.value,
            lotNumberAddress = _lotNumberAddress.value,
            officeNumber = _centerNumber.value,
            roadNameAddress = _roadNameAddress.value
        ).onSuccess {
            navigationHelper.navigateTo(
                NavigationEvent.NavigateTo(
                    CenterRegisterComplete,
                    R.id.registerCenterInfoFragment
                )
            )
        }.onFailure { errorHandlerHelper.sendError(it) }
    }

    internal fun setRegistrationStep(step: RegistrationStep) {
        _registrationStep.value = step
    }

    internal fun setCenterName(name: String) {
        _centerName.value = name
    }

    internal fun setCenterNumber(phoneNumber: String) {
        if (phoneNumber.isDigitsOnly()) {
            _centerNumber.value = phoneNumber
        }
    }

    internal fun setProfileImageUri(uri: Uri?) {
        _centerProfileImageUri.value = uri
    }

    internal fun setCenterIntroduce(introduce: String) {
        _centerIntroduce.value = introduce
    }

    internal fun setRoadNameAddress(address: String) {
        _roadNameAddress.value = address
    }

    internal fun setLotNumberAddress(address: String) {
        _lotNumberAddress.value = address
    }

    internal fun setCenterDetailAddress(address: String) {
        _centerDetailAddress.value = address
    }
}

enum class RegistrationStep(val step: Int) {
    INFO(1), ADDRESS(2), INTRODUCE(3), SUMMARY(4);

    companion object {
        fun findStep(step: Int): RegistrationStep {
            return RegistrationStep.entries.first { it.step == step }
        }
    }
}