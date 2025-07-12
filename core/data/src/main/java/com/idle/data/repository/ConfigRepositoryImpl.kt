package com.idle.data.repository

import com.idle.domain.model.config.ForceUpdate
import com.idle.domain.repositorry.ConfigRepository
import com.idle.network.model.config.ForceUpdateResponse
import com.idle.network.source.ConfigDataSource
import javax.inject.Inject

class ConfigRepositoryImpl @Inject constructor(
    private val configDataSource: ConfigDataSource,
) : ConfigRepository {
    override suspend fun getForceUpdate(): ForceUpdate = configDataSource.getReferenceType(
        key = ConfigDataSource.FORCE_UPDATE,
        defaultValue = ForceUpdateResponse(),
    ).toVO()
}
