package com.idle.worker.job.posting.detail

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.usecase.map.GetStaticMapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerJobPostingDetailViewModel @Inject constructor(
    private val getStaticMapUseCase: GetStaticMapUseCase,
) : BaseViewModel() {
    private val _staticMap = MutableStateFlow<ByteArray?>(null)
    val staticMap = _staticMap.asStateFlow()

    fun getStaticMap(width: Int, height: Int) = viewModelScope.launch {
        getStaticMapUseCase(
            center = "129.091799, 35.132965",
            width = width,
            height = height,
            level = 15,
        ).onSuccess { _staticMap.value = it }
            .onFailure { Log.d("test", "지도 정보를 부르는데 실패하였습니다.") }
    }

}