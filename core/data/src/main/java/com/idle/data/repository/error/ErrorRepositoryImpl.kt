package com.idle.data.repository.error

import com.idle.domain.repositorry.error.ErrorRepository
import com.idle.network.source.error.ErrorLoggingHelper
import javax.inject.Inject

class ErrorRepositoryImpl @Inject constructor(
    private val errorLoggingHelper: ErrorLoggingHelper,
) : ErrorRepository {
    override fun logError(exception: Throwable) = errorLoggingHelper.logError(exception)
}
