package com.idle.domain.repositorry.profile

import com.idle.domain.model.profile.CenterProfile

interface CenterProfileRepository {
    suspend fun getMyCenterProfile(): Result<CenterProfile>

    suspend fun updateMyCenterProfile(officeNumber: String, introduce: String?): Result<Unit>
}