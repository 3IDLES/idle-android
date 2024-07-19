package com.idle.domain.repositorry.profile

import com.idle.domain.model.profile.CenterProfile

interface CenterProfileRepository {
    suspend fun getMyCenterProfile(): Result<CenterProfile>

    suspend fun updateMyCenterProfile(
        centerName: String,
        officeNumber: String,
        lotNumberAddress: String,
        detailedAddress: String,
        longitude: Double,
        latitude: Double,
        introduce: String?,
        profileImageUrl: String?,
    ): Result<Unit>
}