package com.idle.domain.usecase.config

import com.idle.domain.repositorry.config.ConfigRepository
import javax.inject.Inject

class ShowNotificationCenterUseCase @Inject constructor(
    private val configRepository: ConfigRepository,
) {
    suspend operator fun invoke() = configRepository.showNotificationCenter()
}