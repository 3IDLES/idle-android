package com.idle.worker.job.posting

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.CrawlingJobPosting
import com.idle.domain.model.jobposting.JobPosting
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.jobposting.WorkerJobPosting
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.usecase.jobposting.AddFavoriteJobPostingUseCase
import com.idle.domain.usecase.jobposting.ApplyJobPostingUseCase
import com.idle.domain.usecase.jobposting.GetJobPostingsAppliedUseCase
import com.idle.domain.usecase.jobposting.GetMyFavoritesCrawlingJobPostingsUseCase
import com.idle.domain.usecase.jobposting.GetMyFavoritesJobPostingsUseCase
import com.idle.domain.usecase.jobposting.RemoveFavoriteJobPostingUseCase
import com.idle.domain.usecase.profile.GetLocalMyWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorkerJobPostingViewModel @Inject constructor(
    private val getLocalMyWorkerProfileUseCase: GetLocalMyWorkerProfileUseCase,
    private val getJobPostingsAppliedUseCase: GetJobPostingsAppliedUseCase,
    private val getMyFavoritesJobPostingsUseCase: GetMyFavoritesJobPostingsUseCase,
    private val getMyFavoritesCrawlingJobPostingsUseCase: GetMyFavoritesCrawlingJobPostingsUseCase,
    private val applyJobPostingUseCase: ApplyJobPostingUseCase,
    private val addFavoriteJobPostingUseCase: AddFavoriteJobPostingUseCase,
    private val removeFavoriteJobPostingUseCase: RemoveFavoriteJobPostingUseCase,
) : BaseViewModel() {
    private val _profile = MutableStateFlow<WorkerProfile?>(null)
    val profile = _profile.asStateFlow()

    private val _recruitmentPostStatus = MutableStateFlow(RecruitmentPostStatus.APPLY)
    val recruitmentPostStatus = _recruitmentPostStatus.asStateFlow()

    private val next = MutableStateFlow<String?>(null)

    private val _appliedJobPostings = MutableStateFlow<List<JobPosting>?>(null)
    val appliedJobPostings = _appliedJobPostings.asStateFlow()

    private val _favoriteJobPostings = MutableStateFlow<List<JobPosting>?>(null)
    val favoritesJobPostings = _favoriteJobPostings.asStateFlow()

    private var appliedJobPostingCallType: JobPostingCallType = JobPostingCallType.IN_APP

    private var isLoading = false

    init {
        viewModelScope.launch {
            getLocalMyWorkerProfileUseCase().onSuccess {
                _profile.value = it
            }
        }
    }

    internal fun setRecruitmentPostStatus(recruitmentPostStatus: RecruitmentPostStatus) {
        _recruitmentPostStatus.value = recruitmentPostStatus
    }

    internal fun clearJobPostingStatus() {
        _appliedJobPostings.value = null
        _favoriteJobPostings.value = null
        appliedJobPostingCallType = JobPostingCallType.IN_APP
    }

    internal fun getAppliedJobPostings() = viewModelScope.launch {
        if (isLoading) {
            return@launch
        }
        isLoading = true

        try {
            if (appliedJobPostingCallType == JobPostingCallType.END) {
                return@launch
            }

            getJobPostingsAppliedUseCase(next = next.value).onSuccess { (nextId, postings) ->
                next.value = nextId

                if (nextId == null) {
                    appliedJobPostingCallType = JobPostingCallType.END
                }

                _appliedJobPostings.value = _appliedJobPostings.value?.plus(postings) ?: postings
            }.onFailure {
                handleFailure(it as HttpResponseException)
            }
        } finally {
            isLoading = false
        }
    }

    fun getMyFavoritesJobPostings() = viewModelScope.launch {
        getFavoriteCareMeetJobPostings()
        launch { getFavoriteCrawlingJobPostings() }
    }

    private suspend fun getFavoriteCareMeetJobPostings() {
        getMyFavoritesJobPostingsUseCase().onSuccess { postings ->
            _favoriteJobPostings.value = _favoriteJobPostings.value?.plus(postings) ?: postings
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    private suspend fun getFavoriteCrawlingJobPostings() {
        getMyFavoritesCrawlingJobPostingsUseCase().onSuccess { postings ->
            _favoriteJobPostings.value = _favoriteJobPostings.value?.plus(postings) ?: postings
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    internal fun applyJobPosting(jobPostingId: String) = viewModelScope.launch {
        applyJobPostingUseCase(
            jobPostingId = jobPostingId,
            applyMethod = ApplyMethod.APP
        ).onSuccess {
            baseEvent(CareBaseEvent.ShowSnackBar("지원이 완료되었어요.|SUCCESS"))

            _appliedJobPostings.value = _appliedJobPostings.value?.map {
                if (it.jobPostingType == JobPostingType.CAREMEET && it.id == jobPostingId) {
                    val jobPosting = it as WorkerJobPosting
                    jobPosting.copy(applyTime = LocalDateTime.now())
                } else it
            }

            _favoriteJobPostings.value = _favoriteJobPostings.value?.map {
                if (it.jobPostingType == JobPostingType.CAREMEET && it.id == jobPostingId) {
                    val jobPosting = it as WorkerJobPosting
                    jobPosting.copy(applyTime = LocalDateTime.now())
                } else it
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
            baseEvent(CareBaseEvent.ShowSnackBar("즐겨찾기에 추가되었어요.|SUCCESS"))

            _appliedJobPostings.value = _appliedJobPostings.value?.map {
                when (it.jobPostingType) {
                    JobPostingType.CAREMEET -> {
                        it as WorkerJobPosting
                        if (it.id == jobPostingId) it.copy(isFavorite = true) else it
                    }

                    else -> {
                        it as CrawlingJobPosting
                        if (it.id == jobPostingId) it.copy(isFavorite = true) else it
                    }
                }
            }

            _favoriteJobPostings.value = _favoriteJobPostings.value?.map {
                when (it.jobPostingType) {
                    JobPostingType.CAREMEET -> {
                        it as WorkerJobPosting
                        if (it.id == jobPostingId) it.copy(isFavorite = true) else it
                    }

                    else -> {
                        it as CrawlingJobPosting
                        if (it.id == jobPostingId) it.copy(isFavorite = true) else it
                    }
                }
            }
        }.onFailure { handleFailure(it as HttpResponseException) }
    }

    internal fun removeFavoriteJobPosting(jobPostingId: String) = viewModelScope.launch {
        removeFavoriteJobPostingUseCase(jobPostingId = jobPostingId).onSuccess {
            baseEvent(CareBaseEvent.ShowSnackBar("즐겨찾기에서 제거했어요.|SUCCESS"))

            _appliedJobPostings.value = _appliedJobPostings.value?.map {
                when (it.jobPostingType) {
                    JobPostingType.CAREMEET -> {
                        it as WorkerJobPosting
                        if (it.id == jobPostingId) it.copy(isFavorite = false) else it
                    }

                    else -> {
                        it as CrawlingJobPosting
                        if (it.id == jobPostingId) it.copy(isFavorite = false) else it
                    }
                }
            }

            _favoriteJobPostings.value = _favoriteJobPostings.value?.map {
                when (it.jobPostingType) {
                    JobPostingType.CAREMEET -> {
                        it as WorkerJobPosting
                        if (it.id == jobPostingId) it.copy(isFavorite = false) else it
                    }

                    else -> {
                        it as CrawlingJobPosting
                        if (it.id == jobPostingId) it.copy(isFavorite = false) else it
                    }
                }
            }
        }.onFailure { handleFailure(it as HttpResponseException) }
    }
}

enum class RecruitmentPostStatus(val displayName: String) {
    APPLY("지원한 공고"),
    MARKED("찜한 공고")
}

enum class JobPostingCallType {
    IN_APP, CRAWLING, END
}