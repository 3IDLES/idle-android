package com.idle.center.jobposting.complete

import androidx.lifecycle.ViewModel
import com.idle.domain.repositorry.logging.LoggingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpCompleteViewModel @Inject constructor(
    private val loggingRepository: LoggingRepository,
    val navigationHelper: com.idle.navigation.NavigationHelper
) : ViewModel() {
    fun getCenterSignUpProcessStep() = loggingRepository.centerSignUpProcess
}
