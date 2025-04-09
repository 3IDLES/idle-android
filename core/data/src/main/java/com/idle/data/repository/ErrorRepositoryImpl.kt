package com.idle.data.repository

import com.idle.domain.repositorry.ErrorRepository
import com.idle.network.source.error.ErrorLoggingHelper
import javax.inject.Inject

class ErrorRepositoryImpl @Inject constructor(
    private val errorLoggingHelper: ErrorLoggingHelper,
) : ErrorRepository {
    override fun logError(exception: Throwable) = errorLoggingHelper.logError(exception)
}
