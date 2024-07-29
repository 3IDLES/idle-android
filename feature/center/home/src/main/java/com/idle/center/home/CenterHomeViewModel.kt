package com.idle.center.home

import com.idle.binding.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CenterHomeViewModel @Inject constructor() : BaseViewModel() {
    private val _recruitmentPostStatus = MutableStateFlow(RecruitmentPostStatus.ONGOING)
    val recruitmentPostStatus = _recruitmentPostStatus.asStateFlow()

    internal fun setRecruitmentPostStatus(recruitmentPostStatus: RecruitmentPostStatus){
        _recruitmentPostStatus.value = recruitmentPostStatus
    }
}

enum class RecruitmentPostStatus {
    ONGOING,
    PREVIOUS
}