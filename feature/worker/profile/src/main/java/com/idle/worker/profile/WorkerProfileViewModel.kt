package com.idle.worker.profile

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.profile.JobSearchStatus
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.usecase.profile.GetLocalMyWorkerProfileUseCase
import com.idle.domain.usecase.profile.GetWorkerProfileUseCase
import com.idle.domain.usecase.profile.UpdateWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerProfileViewModel @Inject constructor(
    private val getLocalMyWorkerProfileUseCase: GetLocalMyWorkerProfileUseCase,
    private val getWorkerProfileUseCase: GetWorkerProfileUseCase,
    private val updateWorkerProfileUseCase: UpdateWorkerProfileUseCase,
) : BaseViewModel() {
    private val _workerProfile = MutableStateFlow<WorkerProfile?>(null)
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

    private val _jobSearchStatus = MutableStateFlow(JobSearchStatus.UNKNOWN)
    val jobSearchStatus = _jobSearchStatus.asStateFlow()

    private val _isUpdateLoading = MutableStateFlow(false)
    val isUpdateLoading = _isUpdateLoading.asStateFlow()

    internal fun getMyWorkerProfile() = viewModelScope.launch {
        getLocalMyWorkerProfileUseCase().onSuccess {
            _workerProfile.value = it
            _workerIntroduce.value = it.introduce ?: ""
            _specialty.value = it.speciality ?: ""
            _profileImageUri.value = it.profileImageUrl?.toUri()
            _experienceYear.value = it.experienceYear
            _roadNameAddress.value = it.roadNameAddress
            _lotNumberAddress.value = it.lotNumberAddress
            _jobSearchStatus.value = it.jobSearchStatus
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    internal fun getWorkerProfile(workerId: String) = viewModelScope.launch {
        getWorkerProfileUseCase(workerId).onSuccess {
            _workerProfile.value = it
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    internal fun updateWorkerProfile() = viewModelScope.launch {
        val workerProfile = _workerProfile.value
        if (workerProfile == null) {
            baseEvent(CareBaseEvent.ShowSnackBar("로딩중입니다."))
            return@launch
        }

        _isUpdateLoading.value = true

        updateWorkerProfileUseCase(
            experienceYear = _experienceYear.value,
            roadNameAddress = _roadNameAddress.value,
            lotNumberAddress = _lotNumberAddress.value,
            speciality = _specialty.value,
            introduce = _workerIntroduce.value.ifBlank { null },
            imageFileUri = _profileImageUri.value?.toString(),
            jobSearchStatus = _jobSearchStatus.value,
        ).onSuccess {
            getMyWorkerProfile()
            baseEvent(CareBaseEvent.ShowSnackBar("정보 수정이 완료되었어요.|SUCCESS"))
            setEditState(false)
        }.onFailure {
            handleFailure(it as HttpResponseException)
        }.also {
            _isUpdateLoading.value = false
        }
    }

    internal fun setSpecialty(number: String) {
        _specialty.value = number
    }

    internal fun setWorkerIntroduce(introduce: String) {
        _workerIntroduce.value = introduce
    }

    internal fun setEditState(state: Boolean) {
        _isEditState.value = state
        if (!state) {
            getMyWorkerProfile()
        }
    }

    internal fun setProfileImageUrl(uri: Uri?) {
        _profileImageUri.value = uri
    }

    internal fun setRoadNameAddress(roadNameAddress: String) {
        _roadNameAddress.value = roadNameAddress
    }

    internal fun setLotNumberAddress(lotNumberAddress: String) {
        _lotNumberAddress.value = lotNumberAddress
    }

    internal fun setExperienceYear(experienceYear: Int) {
        _experienceYear.value = experienceYear
    }

    internal fun setJobSearchStatus(jobSearchStatus: JobSearchStatus) {
        _jobSearchStatus.value = jobSearchStatus
    }
}