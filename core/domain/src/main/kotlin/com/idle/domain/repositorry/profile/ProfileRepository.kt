package com.idle.domain.repositorry.profile

import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.JobSearchStatus
import com.idle.domain.model.profile.WorkerProfile

interface ProfileRepository {
    suspend fun getMyCenterProfile(): Result<CenterProfile>

    suspend fun getMyWorkerProfile(): Result<WorkerProfile>

    suspend fun getWorkerProfile(workerId: String): Result<WorkerProfile>

    suspend fun updateCenterProfile(officeNumber: String, introduce: String?): Result<Unit>

    suspend fun updateWorkerProfile(
        experienceYear: Int?,
        roadNameAddress: String,
        lotNumberAddress: String,
        jobSearchStatus: JobSearchStatus,
        introduce: String?,
        speciality: String,
    ): Result<Unit>

    suspend fun registerCenterProfile(
        centerName: String,
        detailedAddress: String,
        introduce: String,
        lotNumberAddress: String,
        officeNumber: String,
        roadNameAddress: String,
    ): Result<Unit>

    suspend fun updateProfileImage(
        userType: String,
        imageFileUri: String,
    ): Result<Unit>
}