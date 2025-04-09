package com.idle.domain.repositorry

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggingRepository @Inject constructor() {
    var centerSignUpProcess: Int = 0
}
