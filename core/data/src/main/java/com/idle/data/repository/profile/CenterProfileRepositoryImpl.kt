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
        centerName: String,
        officeNumber: String,
        lotNumberAddress: String,
        detailedAddress: String,
        longitude: Double,
        latitude: Double,
        introduce: String?,
        profileImageUrl: String?
    ): Result<Unit> = centerProfileDataSource.updateMyCenterProfile(
        CenterProfileRequest(
            centerName = centerName,
            officeNumber = officeNumber,
            lotNumberAddress = lotNumberAddress,
            detailedAddress = detailedAddress,
            longitude = longitude,
            latitude = latitude,
            introduce = introduce,
            profileImageUrl = profileImageUrl,
        )
    )
}