package com.idle.domain.usecase.profile

import com.idle.domain.model.auth.UserType
import com.idle.domain.model.profile.JobSearchStatus
import com.idle.domain.repositorry.profile.ProfileRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdateWorkerProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(
        experienceYear: Int?,
        roadNameAddress: String,
        lotNumberAddress: String,
        introduce: String?,
        speciality: String,
        jobSearchStatus: JobSearchStatus,
        imageFileUri: String?,
    ) = runCatching {
        coroutineScope {
            val updateProfileJob = launch {
                profileRepository.updateWorkerProfile(
                    experienceYear = experienceYear,
                    roadNameAddress = roadNameAddress,
                    lotNumberAddress = lotNumberAddress,
                    jobSearchStatus = jobSearchStatus,
                    introduce = introduce,
                    speciality = speciality
                ).getOrThrow()
            }

            val updateProfileImageJob = imageFileUri?.let {
                launch {
                    profileRepository.updateProfileImage(
                        userType = UserType.WORKER.apiValue,
                        imageFileUri = imageFileUri,
                        reqWidth = 278,
                        reqHeight = 278,
                    ).getOrThrow()
                }
            }

            updateProfileJob.join()
            updateProfileImageJob?.join()
        }
    }
}