package com.idle.domain.repositorry

import com.idle.domain.model.config.ForceUpdate

interface ConfigRepository {
    suspend fun getForceUpdate(): ForceUpdate
}
