package com.idle.domain.repositorry.profile

import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.ImageFileInfo

interface ProfileRepository {
    suspend fun getMyCenterProfile(): Result<CenterProfile>

    suspend fun updateCenterProfile(officeNumber: String, introduce: String?): Result<Unit>

    suspend fun updateProfileImage(
        userType: String,
        imageFileInfo: ImageFileInfo,
    ): Result<Unit>
}