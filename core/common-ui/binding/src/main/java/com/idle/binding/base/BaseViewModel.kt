package com.idle.binding.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination
import com.idle.domain.model.error.ApiErrorCode
import com.idle.domain.model.error.HttpResponseException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val _baseEventFlow = MutableSharedFlow<CareBaseEvent>()
    val baseEventFlow = _baseEventFlow.asSharedFlow()

    fun baseEvent(event: CareBaseEvent) = viewModelScope.launch {
        _baseEventFlow.emit(event)
    }

    open fun handleFailure(error: HttpResponseException) = viewModelScope.launch {
        when (error.apiErrorCode) {
            ApiErrorCode.TokenDecodeException,
            ApiErrorCode.TokenNotValid,
            ApiErrorCode.TokenExpiredException,
            ApiErrorCode.TokenNotFound,
            ApiErrorCode.NotSupportUserTokenType -> {
                _baseEventFlow.emit(CareBaseEvent.ShowSnackBar(error.apiErrorCode.displayMsg))
                _baseEventFlow.emit(CareBaseEvent.JwtError(error.apiErrorCode.displayMsg))
            }

            else -> _baseEventFlow.emit(CareBaseEvent.ShowSnackBar(error.apiErrorCode.displayMsg))
        }
    }
}

sealed class CareBaseEvent {
    data class NavigateTo(val destination: DeepLinkDestination, val popUpTo: Int? = null) :
        CareBaseEvent()

    data class ShowSnackBar(val msg: String) : CareBaseEvent()
    data class JwtError(val msg: String) : CareBaseEvent()
}