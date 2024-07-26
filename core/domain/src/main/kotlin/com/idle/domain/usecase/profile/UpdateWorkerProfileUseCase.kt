package com.idle.domain.usecase.profile

import com.idle.domain.model.auth.UserRole
import com.idle.domain.repositorry.profile.ProfileRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class UpdateWorkerProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(
        experienceYear: Int?,
        roadNameAddress: String,
        lotNumberAddress: String,
        longitude: String,
        latitude: String,
        introduce: String?,
        speciality: String,
        imageFileUri: String?,
    ) = runCatching {
        coroutineScope {
            val updateProfileDeferred = async {
                profileRepository.updateWorkerProfile(
                    experienceYear = experienceYear,
                    roadNameAddress = roadNameAddress,
                    lotNumberAddress = lotNumberAddress,
                    longitude = longitude,
                    latitude = latitude,
                    introduce = introduce,
                    speciality = speciality
                ).getOrThrow()
            }

            val updateProfileImageDeferred = imageFileUri?.let {
                async {
                    profileRepository.updateProfileImage(
                        userType = UserRole.WORKER.apiValue,
                        imageFileUri = imageFileUri,
                    ).getOrThrow()
                }
            }

            updateProfileDeferred.await()
            updateProfileImageDeferred?.await()
        }
    }
}