package com.idle.worker.job.posting.detail.worker

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.CrawlingJobPostingDetail
import com.idle.domain.model.jobposting.JobPosting
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.jobposting.WorkerJobPostingDetail
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.usecase.jobposting.AddFavoriteJobPostingUseCase
import com.idle.domain.usecase.jobposting.ApplyJobPostingUseCase
import com.idle.domain.usecase.jobposting.GetCrawlingJobPostingsDetailUseCase
import com.idle.domain.usecase.jobposting.GetWorkerJobPostingDetailUseCase
import com.idle.domain.usecase.jobposting.RemoveFavoriteJobPostingUseCase
import com.idle.domain.usecase.profile.GetLocalMyWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorkerJobPostingDetailViewModel @Inject constructor(
    private val getLocalMyWorkerProfileUseCase: GetLocalMyWorkerProfileUseCase,
    private val getWorkerJobPostingDetailUseCase: GetWorkerJobPostingDetailUseCase,
    private val getCrawlingJobPostingsDetailUseCase: GetCrawlingJobPostingsDetailUseCase,
    private val applyJobPostingUseCase: ApplyJobPostingUseCase,
    private val addFavoriteJobPostingUseCase: AddFavoriteJobPostingUseCase,
    private val removeFavoriteJobPostingUseCase: RemoveFavoriteJobPostingUseCase,
) : BaseViewModel() {
    private val _profile = MutableStateFlow<WorkerProfile?>(null)
    val profile = _profile.asStateFlow()

    private val _workerJobPostingDetail = MutableStateFlow<JobPosting?>(null)
    val workerJobPostingDetail = _workerJobPostingDetail.asStateFlow()

    internal fun getMyProfile() = viewModelScope.launch {
        getLocalMyWorkerProfileUseCase().onSuccess {
            _profile.value = it
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    internal fun getJobPostingDetail(
        jobPostingId: String,
        jobPostingType: String,
    ) = viewModelScope.launch {
        when (jobPostingType) {
            JobPostingType.CAREMEET.name -> getWorkerJobPostingDetailUseCase(jobPostingId).onSuccess {
                _workerJobPostingDetail.value = it
            }.onFailure { handleFailure(it as HttpResponseException) }

            JobPostingType.WORKNET.name -> getCrawlingJobPostingsDetailUseCase(jobPostingId).onSuccess {
                _workerJobPostingDetail.value = it
            }.onFailure { handleFailure(it as HttpResponseException) }
        }
    }

    internal fun applyJobPosting(jobPostingId: String, applyMethod: ApplyMethod) =
        viewModelScope.launch {
            applyJobPostingUseCase(
                jobPostingId = jobPostingId,
                applyMethod = applyMethod,
            ).onSuccess {
                if (_workerJobPostingDetail.value?.jobPostingType == JobPostingType.CAREMEET) {
                    _workerJobPostingDetail.value =
                        (_workerJobPostingDetail.value as WorkerJobPostingDetail).copy(applyTime = LocalDateTime.now())
                }
            }.onFailure { handleFailure(it as HttpResponseException) }
        }

    internal fun addFavoriteJobPosting(
        jobPostingId: String,
        jobPostingType: JobPostingType,
    ) = viewModelScope.launch {
        addFavoriteJobPostingUseCase(
            jobPostingId = jobPostingId,
            jobPostingType = jobPostingType,
        ).onSuccess {
            when (jobPostingType) {
                JobPostingType.CAREMEET -> {
                    _workerJobPostingDetail.value =
                        (_workerJobPostingDetail.value as WorkerJobPostingDetail).copy(isFavorite = true)
                }

                else -> {
                    _workerJobPostingDetail.value =
                        (_workerJobPostingDetail.value as CrawlingJobPostingDetail).copy(isFavorite = true)
                }
            }
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    internal fun removeFavoriteJobPosting(
        jobPostingId: String,
        jobPostingType: JobPostingType,
    ) = viewModelScope.launch {
        removeFavoriteJobPostingUseCase(
            jobPostingId = jobPostingId,
            jobPostingType = jobPostingType,
        ).onSuccess {
            when (jobPostingType) {
                JobPostingType.CAREMEET -> {
                    _workerJobPostingDetail.value =
                        (_workerJobPostingDetail.value as WorkerJobPostingDetail).copy(isFavorite = false)
                }

                else -> {
                    _workerJobPostingDetail.value =
                        (_workerJobPostingDetail.value as CrawlingJobPostingDetail).copy(isFavorite = false)
                }
            }
        }.onFailure { handleFailure(it as HttpResponseException) }
    }
}