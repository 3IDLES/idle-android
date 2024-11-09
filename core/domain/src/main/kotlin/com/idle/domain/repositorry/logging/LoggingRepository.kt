package com.idle.domain.repositorry.logging

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggingRepository @Inject constructor() {
    var centerSignUpProcess: Int = 0
}