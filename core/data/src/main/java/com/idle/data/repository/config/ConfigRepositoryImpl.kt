package com.idle.data.repository.config

import com.idle.domain.model.config.ForceUpdate
import com.idle.domain.repositorry.config.ConfigRepository
import com.idle.network.model.config.ForceUpdateResponse
import com.idle.network.source.remoteconfig.ConfigDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConfigRepositoryImpl @Inject constructor(
    private val configDataSource: ConfigDataSource,
) : ConfigRepository {
    override suspend fun getForceUpdate(): Result<ForceUpdate> = runCatching {
        withContext(Dispatchers.IO) {
            configDataSource.getReferenceType(
                key = ConfigDataSource.FORCE_UPDATE,
                defaultValue = ForceUpdateResponse(),
            ).toVO()
        }
    }
}