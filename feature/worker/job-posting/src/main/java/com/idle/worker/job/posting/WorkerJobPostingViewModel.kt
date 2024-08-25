package com.idle.worker.job.posting

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.CareBaseEvent
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.jobposting.WorkerJobPosting
import com.idle.domain.usecase.jobposting.AddFavoriteJobPostingUseCase
import com.idle.domain.usecase.jobposting.ApplyJobPostingUseCase
import com.idle.domain.usecase.jobposting.GetJobPostingsAppliedUseCase
import com.idle.domain.usecase.jobposting.GetMyFavoritesJobPostingsUseCase
import com.idle.domain.usecase.jobposting.RemoveFavoriteJobPostingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerJobPostingViewModel @Inject constructor(
    private val getJobPostingsAppliedUseCase: GetJobPostingsAppliedUseCase,
    private val getMyFavoritesJobPostingsUseCase: GetMyFavoritesJobPostingsUseCase,
    private val applyJobPostingUseCase: ApplyJobPostingUseCase,
    private val addFavoriteJobPostingUseCase: AddFavoriteJobPostingUseCase,
    private val removeFavoriteJobPostingUseCase: RemoveFavoriteJobPostingUseCase,

    ) : BaseViewModel() {
    private val _recruitmentPostStatus = MutableStateFlow(RecruitmentPostStatus.APPLY)
    val recruitmentPostStatus = _recruitmentPostStatus.asStateFlow()

    private val next = MutableStateFlow<String?>(null)

    private val _appliedJobPostings = MutableStateFlow<List<WorkerJobPosting>>(emptyList())
    val appliedJobPostings = _appliedJobPostings.asStateFlow()

    private val _favoritesJobPostings = MutableStateFlow<List<WorkerJobPosting>>(emptyList())
    val favoritesJobPostings = _favoritesJobPostings.asStateFlow()

    private var appliedJobPostingCallType: JobPostingCallType = JobPostingCallType.IN_APP
    private var favoriteJobPostingCallType: JobPostingCallType = JobPostingCallType.IN_APP

    internal fun setRecruitmentPostStatus(recruitmentPostStatus: RecruitmentPostStatus) {
        _recruitmentPostStatus.value = recruitmentPostStatus
    }

    internal fun clearJobPostingStatus() {
        _appliedJobPostings.value = emptyList()
        _favoritesJobPostings.value = emptyList()
    }

    internal fun getAppliedJobPostings() = viewModelScope.launch {
        getJobPostingsAppliedUseCase(next = next.value).onSuccess { (nextId, postings) ->
            next.value = nextId

            if (nextId == null) {
                appliedJobPostingCallType = JobPostingCallType.END
            }

            _appliedJobPostings.value += postings
        }.onFailure {
            baseEvent(CareBaseEvent.Error(it.message.toString()))
        }
    }

    fun getMyFavoritesJobPostings() = viewModelScope.launch {
        if (favoriteJobPostingCallType == JobPostingCallType.END) return@launch

        when (favoriteJobPostingCallType) {
            JobPostingCallType.IN_APP -> fetchInAppJobPostings()
            JobPostingCallType.CRAWLING -> fetchCrawlingJobPostings()
            JobPostingCallType.END -> return@launch
        }
    }

    private suspend fun fetchInAppJobPostings() {
        getMyFavoritesJobPostingsUseCase(next = next.value).onSuccess { (nextId, postings) ->
            next.value = nextId
            if (nextId == null) {
                favoriteJobPostingCallType = JobPostingCallType.CRAWLING
            }
            _favoritesJobPostings.value += postings
        }.onFailure {

        }
    }

    private suspend fun fetchCrawlingJobPostings() {
        // Todo: 크롤링 공고 호출 로직 추가
    }

    internal fun applyJobPosting(jobPostingId: String) = viewModelScope.launch {
        applyJobPostingUseCase(
            jobPostingId = jobPostingId,
            applyMethod = ApplyMethod.APP
        ).onSuccess {

        }.onFailure {
            baseEvent(CareBaseEvent.Error(it.message.toString()))
        }
    }

    internal fun addFavoriteJobPosting(jobPostingId: String) = viewModelScope.launch {
        addFavoriteJobPostingUseCase(jobPostingId).onSuccess {

        }.onFailure {
            baseEvent(CareBaseEvent.Error(it.message.toString()))
        }
    }

    internal fun removeFavoriteJobPosting(jobPostingId: String) = viewModelScope.launch {
        removeFavoriteJobPostingUseCase(jobPostingId).onSuccess {

        }.onFailure {
            baseEvent(CareBaseEvent.Error(it.message.toString()))
        }
    }
}

enum class RecruitmentPostStatus(val displayName: String) {
    APPLY("지원한 공고"),
    MARKED("찜한 공고")
}

enum class JobPostingCallType {
    IN_APP, CRAWLING, END
}