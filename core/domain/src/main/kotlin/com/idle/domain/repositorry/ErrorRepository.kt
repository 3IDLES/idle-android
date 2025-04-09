package com.idle.domain.repositorry

interface ErrorRepository {
    fun logError(exception: Throwable)
}
