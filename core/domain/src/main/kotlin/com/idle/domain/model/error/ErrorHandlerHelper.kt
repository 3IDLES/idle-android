package com.idle.domain.model.error

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandlerHelper @Inject constructor() {
    private val _errorEvent = MutableSharedFlow<Throwable>(extraBufferCapacity = 1)
    val errorEvent = _errorEvent.asSharedFlow()

    fun sendError(error: Throwable) {
        _errorEvent.tryEmit(error)
    }
}
