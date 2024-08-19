package com.idle.binding.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import com.idle.domain.model.error.HttpResponseException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val _baseEventFlow = MutableSharedFlow<CareBaseEvent>()
    val baseEventFlow = _baseEventFlow.asSharedFlow()

    private val _errorFlow = MutableSharedFlow<HttpResponseException>()
    val errorFlow = _errorFlow.asSharedFlow()

    fun baseEvent(event: CareBaseEvent) = viewModelScope.launch {
        _baseEventFlow.emit(event)
    }

    fun error(error: HttpResponseException) = viewModelScope.launch {
        _errorFlow.emit(error)
    }
}

sealed class CareBaseEvent {
    data class NavigateTo(
        val destination: DeepLinkDestination,
        val popUpTo: Int? = null,
    ) : CareBaseEvent()

    data class Error(val message: String) : CareBaseEvent()
}