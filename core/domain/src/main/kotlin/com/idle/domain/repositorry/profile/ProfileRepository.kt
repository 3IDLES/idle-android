package com.idle.domain.repositorry.profile

import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.WorkerProfile

interface ProfileRepository {
    suspend fun getMyCenterProfile(): Result<CenterProfile>

    suspend fun getMyWorkerProfile(): Result<WorkerProfile>

    suspend fun updateCenterProfile(officeNumber: String, introduce: String?): Result<Unit>

    suspend fun updateProfileImage(
        userType: String,
        imageFileUri: String,
    ): Result<Unit>
}