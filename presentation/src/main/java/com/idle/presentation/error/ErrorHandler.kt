package com.idle.presentation.error

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor() {
    private val _errorFlow = MutableStateFlow<Exception?>(null)
    val errorFlow = _errorFlow.asStateFlow()

    fun sendError(error: Exception) {
        _errorFlow.value = error
    }
}