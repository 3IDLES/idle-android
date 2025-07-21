package com.idle.domain.repositorry

import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.CenterRegistrationStatus
import com.idle.domain.model.profile.JobSearchStatus
import com.idle.domain.model.profile.WorkerProfile

interface ProfileRepository {
    suspend fun getMyUserType(): String
    suspend fun getMyCenterProfile(): CenterProfile
    suspend fun getCenterProfile(centerId: String): CenterProfile
    suspend fun getMyWorkerProfile(): WorkerProfile
    suspend fun getWorkerProfile(workerId: String): WorkerProfile
    suspend fun getWorkerId(): String
    suspend fun getCenterStatus(): CenterRegistrationStatus
    suspend fun updateCenterProfile(
        officeNumber: String,
        introduce: String?,
        imageFileUri: String?,
    )

    suspend fun updateWorkerProfile(
        experienceYear: Int?,
        roadNameAddress: String,
        lotNumberAddress: String,
        jobSearchStatus: JobSearchStatus,
        introduce: String?,
        speciality: String,
        imageFileUri: String?,
    )

    suspend fun registerCenterProfile(
        centerName: String,
        detailedAddress: String,
        introduce: String,
        lotNumberAddress: String,
        officeNumber: String,
        roadNameAddress: String,
        imageFileUri: String?,
    )
}
