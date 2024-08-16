package com.idle.worker.job.posting.detail.center

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.jobposting.CenterJobPostingDetail
import com.idle.domain.usecase.jobposting.GetCenterJobPostingDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterJobPostingDetailViewModel @Inject constructor(
    private val getCenterJobPostingDetailUseCase: GetCenterJobPostingDetailUseCase,
) : BaseViewModel() {
    private val _jobPostingDetail = MutableStateFlow<CenterJobPostingDetail?>(null)
    val jobPostingDetail = _jobPostingDetail.asStateFlow()

    fun getCenterJobPostingDetail(jobPostingId: String) = viewModelScope.launch {
        Log.d("test", "$jobPostingId 를 조회해보자!")

        getCenterJobPostingDetailUseCase(jobPostingId).onSuccess { _jobPostingDetail.value = it }
            .onFailure { Log.d("test", "센터 공고 상세 조회 실패! $it") }
    }
}