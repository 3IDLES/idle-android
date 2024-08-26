package com.idle.worker.profile

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.usecase.profile.GetLocalMyWorkerProfileUseCase
import com.idle.domain.usecase.profile.UpdateWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerProfileViewModel @Inject constructor(
    private val getLocalMyWorkerProfileUseCase: GetLocalMyWorkerProfileUseCase,
    private val updateWorkerProfileUseCase: UpdateWorkerProfileUseCase,
) : BaseViewModel() {
    private val _workerProfile = MutableStateFlow(WorkerProfile())
    val workerProfile = _workerProfile.asStateFlow()

    private val _specialty = MutableStateFlow("")
    val specialty = _specialty.asStateFlow()

    private val _workerIntroduce = MutableStateFlow("")
    val workerIntroduce = _workerIntroduce.asStateFlow()

    private val _experienceYear = MutableStateFlow<Int?>(null)
    val experienceYear = _experienceYear.asStateFlow()

    private val _roadNameAddress = MutableStateFlow("")
    val roadNameAddress = _roadNameAddress.asStateFlow()

    private val _lotNumberAddress = MutableStateFlow("")

    private val _isEditState = MutableStateFlow(false)
    val isEditState = _isEditState.asStateFlow()

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri = _profileImageUri.asStateFlow()

    private val _gender = MutableStateFlow(Gender.NONE)
    internal val gender = _gender.asStateFlow()

    init {
        getMyWorkerProfile()
    }

    private fun getMyWorkerProfile() = viewModelScope.launch {
        getLocalMyWorkerProfileUseCase().onSuccess {
            _workerProfile.value = it
            _workerIntroduce.value = it.introduce ?: ""
            _specialty.value = it.speciality ?: ""
            _gender.value = it.gender
            _profileImageUri.value = it.profileImageUrl?.toUri()
            _experienceYear.value = it.experienceYear
        }.onFailure { baseEvent(CareBaseEvent.Error(it.message.toString())) }
    }

    fun updateWorkerProfile() = viewModelScope.launch {
        if (_specialty.value.isBlank()) {
            return@launch
        }

        updateWorkerProfileUseCase(
            experienceYear = _experienceYear.value,
            roadNameAddress = "",
            lotNumberAddress = "",
            speciality = _specialty.value,
            introduce = _workerIntroduce.value.ifBlank { null },
            imageFileUri = _profileImageUri.value?.toString(),
            jobSearchStatus = _workerProfile.value.jobSearchStatus,
        ).onSuccess {
            setEditState(false)
        }.onFailure {
            baseEvent(CareBaseEvent.Error(it.message.toString()))
        }
    }

    fun setSpecialty(number: String) {
        _specialty.value = number
    }

    fun setWorkerIntroduce(introduce: String) {
        _workerIntroduce.value = introduce
    }

    fun setEditState(state: Boolean) {
        _isEditState.value = state
    }

    fun setProfileImageUrl(uri: Uri?) {
        _profileImageUri.value = uri
    }

    internal fun setGender(gender: Gender) {
        _gender.value = gender
    }

    internal fun setRoadNameAddress(address: String) {
        _roadNameAddress.value = address
    }

    internal fun setLotNumberAddress(address: String) {
        _lotNumberAddress.value = address
    }
}