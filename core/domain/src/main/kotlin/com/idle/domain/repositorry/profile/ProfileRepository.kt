package com.idle.domain.repositorry.profile

import com.idle.domain.model.profile.CenterProfile

interface ProfileRepository {
    suspend fun getMyCenterProfile(): Result<CenterProfile>

    suspend fun updateCenterProfile(officeNumber: String, introduce: String?): Result<Unit>

    suspend fun updateCenterProfileImage(
        userType: String,
        imageFileExtension: String
    ): Result<Unit>
}