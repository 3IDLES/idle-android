package com.idle.worker.home

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.jobposting.JobPosting
import com.idle.domain.usecase.jobposting.GetJobPostingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerHomeViewModel @Inject constructor(
    private val getJobPostingsUseCase: GetJobPostingsUseCase
) : BaseViewModel() {
    private val next = MutableStateFlow<String?>(null)

    private val _jobPostings = MutableStateFlow<List<JobPosting>>(listOf())
    val jobPostings = _jobPostings.asStateFlow()

    init{
        getJobPostings()
    }

    fun getJobPostings() = viewModelScope.launch {
        getJobPostingsUseCase(next = next.value).onSuccess {
            next.value = it.first
            _jobPostings.value += it.second
        }.onFailure {

        }
    }
}