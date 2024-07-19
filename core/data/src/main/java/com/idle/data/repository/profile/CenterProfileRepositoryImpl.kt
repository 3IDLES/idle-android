package com.idle.data.repository.profile

import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.repositorry.profile.CenterProfileRepository
import com.idle.network.model.profile.CenterProfileRequest
import com.idle.network.source.CenterProfileDataSource
import javax.inject.Inject

class CenterProfileRepositoryImpl @Inject constructor(
    private val centerProfileDataSource: CenterProfileDataSource,
) : CenterProfileRepository {
    override suspend fun getMyCenterProfile(): Result<CenterProfile> =
        centerProfileDataSource.getMyCenterProfile().mapCatching { it.toVO() }

    override suspend fun updateMyCenterProfile(
        officeNumber: String,
        introduce: String?,
    ): Result<Unit> = centerProfileDataSource.updateMyCenterProfile(
        CenterProfileRequest(
            officeNumber = officeNumber,
            introduce = introduce,
        )
    )
}