package com.idle.presentation.error

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor() {
    private val _baseEventFlow = MutableStateFlow<Exception?>(null)
    val baseEventFlow = _baseEventFlow.asStateFlow()

    fun handleFailure(error: Exception?) {
        _baseEventFlow.value = error
    }
}