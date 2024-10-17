package com.idle.domain.repositorry.config

import com.idle.domain.model.config.ForceUpdate

interface ConfigRepository {
    suspend fun getForceUpdate(): Result<ForceUpdate>
    suspend fun showNotificationCenter(): Result<Boolean>
}