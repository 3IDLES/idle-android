package com.idle.worker.job.posting.detail.worker

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.jobposting.WorkerJobPostingDetail
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.usecase.jobposting.AddFavoriteJobPostingUseCase
import com.idle.domain.usecase.jobposting.ApplyJobPostingUseCase
import com.idle.domain.usecase.jobposting.GetWorkerJobPostingDetailUseCase
import com.idle.domain.usecase.jobposting.RemoveFavoriteJobPostingUseCase
import com.idle.domain.usecase.profile.GetLocalMyWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerJobPostingDetailViewModel @Inject constructor(
    private val getLocalMyWorkerProfileUseCase: GetLocalMyWorkerProfileUseCase,
    private val getWorkerJobPostingDetailUseCase: GetWorkerJobPostingDetailUseCase,
    private val applyJobPostingUseCase: ApplyJobPostingUseCase,
    private val addFavoriteJobPostingUseCase: AddFavoriteJobPostingUseCase,
    private val removeFavoriteJobPostingUseCase: RemoveFavoriteJobPostingUseCase,
) : BaseViewModel() {
    private val _profile = MutableStateFlow<WorkerProfile?>(null)
    val profile = _profile.asStateFlow()

    private val _workerJobPostingDetail = MutableStateFlow<WorkerJobPostingDetail?>(null)
    val workerJobPostingDetail = _workerJobPostingDetail.asStateFlow()

    internal fun getMyProfile() = viewModelScope.launch {
        getLocalMyWorkerProfileUseCase().onSuccess {
            _profile.value = it
        }.onFailure {
            baseEvent(CareBaseEvent.ShowSnackBar(it.message.toString()))
        }
    }

    internal fun getJobPostingDetail(jobPostingId: String) = viewModelScope.launch {
        getWorkerJobPostingDetailUseCase(jobPostingId).onSuccess {
            _workerJobPostingDetail.value = it
        }.onFailure {

        }
    }

    internal fun applyJobPosting(jobPostingId: String, applyMethod: ApplyMethod) =
        viewModelScope.launch {
            applyJobPostingUseCase(
                jobPostingId = jobPostingId,
                applyMethod = applyMethod,
            ).onSuccess {

            }.onFailure {
                baseEvent(CareBaseEvent.ShowSnackBar(it.message.toString()))
            }
        }

    internal fun addFavoriteJobPosting(jobPostingId: String) = viewModelScope.launch {
        addFavoriteJobPostingUseCase(jobPostingId).onSuccess {

        }.onFailure {
            baseEvent(CareBaseEvent.ShowSnackBar(it.message.toString()))
        }
    }

    internal fun removeFavoriteJobPosting(jobPostingId: String) = viewModelScope.launch {
        removeFavoriteJobPostingUseCase(jobPostingId).onSuccess {

        }.onFailure {
            baseEvent(CareBaseEvent.ShowSnackBar(it.message.toString()))
        }
    }
}