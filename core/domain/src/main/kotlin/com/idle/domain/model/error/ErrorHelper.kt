package com.idle.domain.model.error

import com.idle.domain.repositorry.error.ErrorRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHelper @Inject constructor(private val errorRepository: ErrorRepository) {
    private val _errorEvent = Channel<Throwable>(BUFFERED)
    val errorEvent = _errorEvent.receiveAsFlow()

    suspend fun sendError(error: Throwable) {
        logError(error)
        _errorEvent.send(error)
    }

    fun logError(error: Throwable) = errorRepository.logError(exception = error)
}
