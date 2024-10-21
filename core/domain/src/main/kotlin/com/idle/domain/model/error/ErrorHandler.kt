package com.idle.domain.model.error

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor() {
    private val _errorEvent = MutableStateFlow<Throwable?>(null)
    val errorEvent = _errorEvent.asStateFlow()

    fun sendError(error: Throwable) {
        _errorEvent.value = error
    }
}
