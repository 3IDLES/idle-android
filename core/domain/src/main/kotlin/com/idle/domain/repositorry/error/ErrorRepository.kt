package com.idle.domain.repositorry.error

interface ErrorRepository {
    fun logError(exception: Throwable)
}
